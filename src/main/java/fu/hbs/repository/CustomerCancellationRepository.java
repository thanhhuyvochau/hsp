package fu.hbs.repository;

import fu.hbs.entities.CustomerCancellation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerCancellationRepository extends JpaRepository<CustomerCancellation, Long> {

    CustomerCancellation findByCancellationId(Long id);


}
