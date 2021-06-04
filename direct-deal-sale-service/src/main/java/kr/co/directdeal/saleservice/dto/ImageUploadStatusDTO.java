package kr.co.directdeal.saleservice.dto;

import java.time.Instant;

import kr.co.directdeal.saleservice.domain.ImageUploadStatus.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageUploadStatusDTO {
    private String id;
    private Status status;
    private Instant createdDate; 
}
