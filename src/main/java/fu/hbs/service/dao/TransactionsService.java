package fu.hbs.service.dao;

import fu.hbs.dto.VnpayDTO.ViewPaymentDTO;
import fu.hbs.entities.Transactions;


import java.time.LocalDate;
import java.util.List;

public interface TransactionsService {
    Transactions save(Transactions vnpayTransactions);

    List<ViewPaymentDTO> findByCreateDateAndPaymentId(LocalDate createDate, Long paymentId);

    Transactions findFirstTransactionOfHotelBooking(Long hotelBookingId);

}
