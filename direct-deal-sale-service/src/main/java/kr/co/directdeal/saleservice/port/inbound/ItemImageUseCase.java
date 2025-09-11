package kr.co.directdeal.saleservice.port.inbound;

import kr.co.directdeal.saleservice.application.service.dto.ImageUploadStatusDTO;
import kr.co.directdeal.saleservice.application.service.dto.ItemImageDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemImageUseCase {
    byte[] readImage(String filename);

    ItemImageDTO saveImages(List<MultipartFile> files);

    ImageUploadStatusDTO checkUploadStatus(String id);
}
