package kr.co.directdeal.saleservice.port.inbound;

import kr.co.directdeal.saleservice.application.service.dto.ItemDTO;
import org.springframework.web.bind.annotation.PathVariable;

public interface ItemUseCase {
    void register(ItemDTO itemDTO);

    void update(ItemDTO itemDTO);

    void delete(@PathVariable("id") String id);

    void sale(String id);

    void pause(String id);

    void complete(String id);
}
