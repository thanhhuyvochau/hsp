package fu.hbs.repository;

import fu.hbs.entities.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {


    @Query(value = "SELECT * \n" +
            "FROM transactions \n" +
            "WHERE DATE(created_date) = ?1  AND payment_id = ?2 ; ", nativeQuery = true)
    List<Transactions> findTransactionsCreatedDateAndPaymentId(LocalDate createDate, Long paymentId);


    Optional<Transactions> findByHotelBookingIdAndTransactionTypeId(Long hotelBookingId, Long transactionTypeId);
}
