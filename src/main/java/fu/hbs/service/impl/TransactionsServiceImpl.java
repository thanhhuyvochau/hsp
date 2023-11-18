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

import fu.hbs.dto.VnpayDTO.ViewPaymentDTO;
import fu.hbs.entities.PaymentType;
import fu.hbs.entities.Transactions;
import fu.hbs.repository.PaymentTypeRepository;
import fu.hbs.repository.TransactionsRepository;
import fu.hbs.service.dao.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionsServiceImpl implements TransactionsService {
    @Autowired
    TransactionsRepository transactionsRepository;
    @Autowired
    PaymentTypeRepository paymentTypeRepository;

    @Override
    public Transactions save(Transactions vnpayTransactions) {
        return transactionsRepository.save(vnpayTransactions);
    }

    @Override
    public List<ViewPaymentDTO> findByCreateDateAndPaymentId(LocalDate createDate, Long paymentId) {


        Transactions transactions = new Transactions();
        List<ViewPaymentDTO> viewPaymentDTOList = new ArrayList<>();
        PaymentType paymentType = new PaymentType();
        List<Transactions> vnpayTransactionsList = transactionsRepository.findTransactionsCreatedDateAndPaymentId(createDate, paymentId);
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
}
