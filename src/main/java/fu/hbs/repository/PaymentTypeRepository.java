package fu.hbs.repository;

import fu.hbs.entities.BankList;
import fu.hbs.entities.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentType, Long> {
}
