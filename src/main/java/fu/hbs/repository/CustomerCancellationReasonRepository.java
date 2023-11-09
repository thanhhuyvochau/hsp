package fu.hbs.repository;

import fu.hbs.entities.customerCancellationReasons;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerCancellationReasonRepository extends JpaRepository<customerCancellationReasons, Long> {
}
