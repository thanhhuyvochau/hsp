package fu.hbs.repository;

import fu.hbs.entities.CustomerCancellation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerCancellationRepository extends JpaRepository<CustomerCancellation, Long> {

    CustomerCancellation findByCancellationId(Long id);

    CustomerCancellation findCustomerCancellationByAccountIdAndHotelBookingId(Long accId, Long hotelBookingId);

    @Query(value = "SELECT ra.*\n" +
            "            FROM hotelsystem.customer_cancellation ra\n" +
            "            WHERE hotel_booking_id = ?1 \n" +
            "            ORDER BY account_id DESC\n" +
            "            LIMIT 1", nativeQuery = true)
    CustomerCancellation findCustomerCancellationNewHotelBookingId(Long hotelBookingId);

    @Query(value = " SELECT * FROM hotelsystem.customer_cancellation\n" +
            "            WHERE status = 0", nativeQuery = true)
    List<CustomerCancellation> findByStatus();

    @Query(value = " SELECT * FROM hotelsystem.customer_cancellation\n" +
            "            WHERE status = 1 ", nativeQuery = true)
    List<CustomerCancellation> findByStatusConfirm();


}
