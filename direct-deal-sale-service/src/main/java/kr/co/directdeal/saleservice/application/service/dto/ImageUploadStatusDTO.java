package kr.co.directdeal.saleservice.application.service.dto;

import java.time.Instant;

import kr.co.directdeal.saleservice.domain.object.ImageUploadStatus.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing the status of an image upload.
 * Contains the identifier, current status, and creation timestamp of the upload.
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageUploadStatusDTO {

    /**
     * Unique identifier of the image upload.
     */
    private String id;

    /**
     * Current status of the image upload.
     */
    private Status status;

    /**
     * Timestamp indicating when the image upload status was created.
     */
    private Instant createdDate;
}
