package kr.co.directdeal.saleservice.domain.object;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity representing the status of an image upload process.
 *
 * This entity stores the current state of an image upload operation,
 * including whether it is processing, succeeded, or failed, along with
 * the timestamp of creation.
 *
 * @author Cheol Jeon
 */
@Entity
@Table(name = "IMAGE_UPLOAD_STATUS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class ImageUploadStatus {
    /**
     * Unique identifier for the image upload status.
     */
    @NotNull
    @Id
    @Column(name = "IMAGE_UPLOAD_STATUS_ID", length = 36)
    private String id;

    /**
     * Current status of the image upload process.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "IMAGE_UPLOAD_STATUS_RESULT", length = 10, nullable = false)
    private Status status;

    /**
     * The timestamp when this status record was created.
     */
    @CreatedDate
    @Column(name = "IMAGE_UPLOAD_STATUS_CREATED_DATE", nullable = false)
    private Instant createdDate;

    /**
     * Enum representing possible image upload statuses.
     */
    public enum Status {
        PROCESSING,
        SUCCESS,
        FAILURE
    }
}
