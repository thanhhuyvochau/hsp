/*
 * Copyright (C) 2023, FPT University
 * SEP490 - SEP490_G77
 * HBS
 * Hotel Booking System
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 08/11/2023    1.0        HieuLBM          First Deploy
 *
 */
package fu.hbs.service.impl;

import fu.hbs.constant.TransactionMessage;
import fu.hbs.dto.VnpayDTO.ViewPaymentDTO;
import fu.hbs.entities.EarlyCheckoutRefunds;
import fu.hbs.entities.PaymentType;
import fu.hbs.entities.Transactions;
import fu.hbs.repository.EarlyCheckoutRefundsRepository;
import fu.hbs.repository.PaymentTypeRepository;
import fu.hbs.repository.TransactionsRepository;
import fu.hbs.service.dao.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionsServiceImpl implements TransactionsService {
    @Autowired
    TransactionsRepository transactionsRepository;
    @Autowired
    PaymentTypeRepository paymentTypeRepository;
    @Autowired
    EarlyCheckoutRefundsRepository earlyCheckoutRefundsRepository;

    @Override
    public Transactions save(Transactions vnpayTransactions) {
        Transactions transactions = transactionsRepository.save(vnpayTransactions);
        if (vnpayTransactions.getTransactionTypeId() == 3L) {
            EarlyCheckoutRefunds earlyCheckoutRefunds = makeEarlyCheckout(transactions);
            earlyCheckoutRefundsRepository.save(earlyCheckoutRefunds);
        }
        return transactions;
    }


    @Override
    public List<ViewPaymentDTO> findByCreateDateAndPaymentId(LocalDate createDate, Long paymentId) {


        Transactions transactions = new Transactions();
        List<ViewPaymentDTO> viewPaymentDTOList = new ArrayList<>();
        PaymentType paymentType = new PaymentType();
        List<Transactions> vnpayTransactionsList =
                transactionsRepository.findTransactionsCreatedDateAndPaymentId(createDate, paymentId);
        for (int i = 0; i < vnpayTransactionsList.size(); i++) {
            ViewPaymentDTO viewPaymentDTO = new ViewPaymentDTO();
            transactions.setPaymentId(vnpayTransactionsList.get(i).getPaymentId());
            transactions.setTransactionId(vnpayTransactionsList.get(i).getTransactionId());
            transactions.setStatus(vnpayTransactionsList.get(i).getStatus());
            transactions.setVnpayTransactionId(vnpayTransactionsList.get(i).getVnpayTransactionId());
            transactions.setCreatedDate(vnpayTransactionsList.get(i).getCreatedDate());
            transactions.setAmount(vnpayTransactionsList.get(i).getAmount());
            transactions.setHotelBookingId(vnpayTransactionsList.get(i).getHotelBookingId());
            paymentType = paymentTypeRepository.findByPaymentId(vnpayTransactionsList.get(i).getPaymentId());

            viewPaymentDTO.setPaymentType(paymentType);
            viewPaymentDTO.setTransactions(transactions);
            viewPaymentDTOList.add(viewPaymentDTO);
        }
        return viewPaymentDTOList;
    }

    @Override
    public Transactions findFirstTransactionOfHotelBooking(Long hotelBookingId) {
        Optional<Transactions> payTransaction =
                transactionsRepository.findByHotelBookingIdAndTransactionTypeId(hotelBookingId,
                        2L);
        if (payTransaction.isPresent()) {
            return payTransaction.get();
        } else {
            payTransaction =
                    transactionsRepository.findByHotelBookingIdAndTransactionTypeId(hotelBookingId, 1L);
            return payTransaction.orElse(null);
        }
    }

    public static EarlyCheckoutRefunds makeEarlyCheckout(Transactions transactions) {
        EarlyCheckoutRefunds earlyCheckoutRefunds = new EarlyCheckoutRefunds();
        earlyCheckoutRefunds.setRefundedAmount(transactions.getAmount().abs());
        earlyCheckoutRefunds.setStatus(true);
        earlyCheckoutRefunds.setTransactionId(transactions.getTransactionId());
        earlyCheckoutRefunds.setReason("");
        earlyCheckoutRefunds.setRefundDate(Instant.now());
        return earlyCheckoutRefunds;
    }
}
