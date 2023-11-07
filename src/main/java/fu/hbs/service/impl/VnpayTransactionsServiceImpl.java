package fu.hbs.service.impl;

import fu.hbs.entities.VnpayTransactions;
import fu.hbs.repository.VnpayTransactionsRepository;
import fu.hbs.service.dao.VnpayTransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VnpayTransactionsServiceImpl implements VnpayTransactionsService {
    @Autowired
    VnpayTransactionsRepository vnpayTransactionsRepository;

    @Override
    public VnpayTransactions save(VnpayTransactions vnpayTransactions) {
        return vnpayTransactionsRepository.save(vnpayTransactions);
    }
}
