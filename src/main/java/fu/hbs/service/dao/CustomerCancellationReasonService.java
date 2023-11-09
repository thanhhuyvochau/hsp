package fu.hbs.service.dao;

import fu.hbs.entities.customerCancellationReasons;

import java.util.List;

public interface CustomerCancellationReasonService {

    List<customerCancellationReasons> findAll();
}
