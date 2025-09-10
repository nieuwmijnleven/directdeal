package kr.co.directdeal.saleservice.application.service.mapper;

import org.springframework.stereotype.Component;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.saleservice.domain.object.ImageUploadStatus;
import kr.co.directdeal.saleservice.application.service.dto.ImageUploadStatusDTO;

@Component
public class ImageUploadStatusMapper implements Mapper<ImageUploadStatus, ImageUploadStatusDTO> {

	@Override
	public ImageUploadStatus toEntity(ImageUploadStatusDTO dto) {
		return ImageUploadStatus.builder()
					.id(dto.getId())
					.status(dto.getStatus())
					.createdDate(dto.getCreatedDate())
					.build();
	}

	@Override
	public ImageUploadStatusDTO toDTO(ImageUploadStatus entity) {
		return ImageUploadStatusDTO.builder()
					.id(entity.getId())
					.status(entity.getStatus())
					.createdDate(entity.getCreatedDate())
					.build();
	}
}
