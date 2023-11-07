package fu.hbs.service.impl;

import fu.hbs.entities.CategoryRoomPrice;
import fu.hbs.repository.CategoryRoomPriceRepository;
import fu.hbs.service.dao.CategoryRoomPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryRoomPriceServiceImpl implements CategoryRoomPriceService {
    @Autowired
    private CategoryRoomPriceRepository categoryRoomPriceRepository;

    @Override
    public CategoryRoomPrice findByCateRoomPriceId(Long id) {
        return categoryRoomPriceRepository.getCategoryId(id);
    }
}
