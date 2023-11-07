package fu.hbs.repository;

import fu.hbs.entities.VnpayTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VnpayTransactionsRepository extends JpaRepository<VnpayTransactions, Long> {
}
