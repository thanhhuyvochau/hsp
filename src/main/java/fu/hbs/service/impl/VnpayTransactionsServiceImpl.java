/*
 * Copyright (C) 2023, FPT University
 * SEP490 - SEP490_G77
 * HBS
 * Hotel Booking System
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 08/11/2023    1.0        HieuLBM          First Deploy
 *
 */
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
