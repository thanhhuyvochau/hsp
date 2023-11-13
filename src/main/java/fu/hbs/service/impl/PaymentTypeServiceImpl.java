package fu.hbs.service.impl;

import fu.hbs.entities.PaymentType;
import fu.hbs.repository.PaymentTypeRepository;
import fu.hbs.service.dao.PaymentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentTypeServiceImpl implements PaymentTypeService {
    @Autowired
    private PaymentTypeRepository paymentTypeRepository;

    @Override
    public List<PaymentType> getAllPaymentType() {
        return paymentTypeRepository.findAll();
    }
}
