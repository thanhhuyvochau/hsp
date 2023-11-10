package fu.hbs.repository;

import fu.hbs.entities.RefundAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundAccountRepository extends JpaRepository<RefundAccount, Long> {
    RefundAccount save(RefundAccount refundAccount);
}
