package kr.co.directdeal.saleservice.port.inbound;

import kr.co.directdeal.saleservice.application.service.dto.ItemCategoryDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemCategoryUseCase {
    List<ItemCategoryDTO> list();

    void insert(ItemCategoryDTO dto);

    void update(ItemCategoryDTO dto);

    void delete(ItemCategoryDTO dto);
}
