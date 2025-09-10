package kr.co.directdeal.saleservice.port.inbound;

import kr.co.directdeal.saleservice.application.service.dto.FavoriteItemDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FavoriteItemUseCase {
    List<FavoriteItemDTO> list(FavoriteItemDTO dto);

    void save(FavoriteItemDTO dto);

    void delete(FavoriteItemDTO dto);
}
