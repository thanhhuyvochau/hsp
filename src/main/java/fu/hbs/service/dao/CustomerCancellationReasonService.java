package fu.hbs.service.dao;

import fu.hbs.entities.CustomerCancellationReasons;

import java.util.List;

public interface CustomerCancellationReasonService {

    List<CustomerCancellationReasons> findAll();
}
