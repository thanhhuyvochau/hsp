package fu.hbs.service.dao;

import fu.hbs.dto.CancellationDTO.ViewCancellationDTO;
import fu.hbs.entities.CustomerCancellation;

import java.util.List;

public interface CustomerCancellationService {
    List<ViewCancellationDTO> getAll();
}
