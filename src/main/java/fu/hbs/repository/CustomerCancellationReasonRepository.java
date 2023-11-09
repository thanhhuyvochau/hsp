package fu.hbs.repository;

import fu.hbs.entities.customerCancellationReasons;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerCancellationReasonRepository extends JpaRepository<customerCancellationReasons, Long> {

}
