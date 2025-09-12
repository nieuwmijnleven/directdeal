package kr.co.directdeal.saleservice.application.service.mapper;

import org.springframework.stereotype.Component;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.saleservice.domain.object.ImageUploadStatus;
import kr.co.directdeal.saleservice.application.service.dto.ImageUploadStatusDTO;

/**
 * Mapper class to convert between ImageUploadStatus entity and ImageUploadStatusDTO.
 * Implements methods toEntity and toDTO for data transformation.
 *
 * @author Cheol Jeon
 */
@Component
public class ImageUploadStatusMapper implements Mapper<ImageUploadStatus, ImageUploadStatusDTO> {

    /**
     * Converts ImageUploadStatusDTO to ImageUploadStatus entity.
     *
     * @param dto the ImageUploadStatusDTO to convert
     * @return the corresponding ImageUploadStatus entity
     */
    @Override
    public ImageUploadStatus toEntity(ImageUploadStatusDTO dto) {
        return ImageUploadStatus.builder()
                .id(dto.getId())
                .status(dto.getStatus())
                .createdDate(dto.getCreatedDate())
                .build();
    }

    /**
     * Converts ImageUploadStatus entity to ImageUploadStatusDTO.
     *
     * @param entity the ImageUploadStatus entity to convert
     * @return the corresponding ImageUploadStatusDTO
     */
    @Override
    public ImageUploadStatusDTO toDTO(ImageUploadStatus entity) {
        return ImageUploadStatusDTO.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .createdDate(entity.getCreatedDate())
                .build();
    }
}
