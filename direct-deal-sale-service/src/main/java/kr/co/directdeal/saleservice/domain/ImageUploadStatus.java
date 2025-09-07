package kr.co.directdeal.saleservice.domain;

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

@Entity
@Table(name = "IMAGE_UPLOAD_STATUS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class ImageUploadStatus {
    @NotNull
    @Id
    @Column(name = "IMAGE_UPLOAD_STATUS_ID", length = 36)
    private String id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "IMAGE_UPLOAD_STATUS_RESULT", length = 10, nullable = false)
    private Status status;

    @CreatedDate
    @Column(name = "IMAGE_UPLOAD_STATUS_CREATED_DATE", nullable = false)
    private Instant createdDate; 

    public enum Status {
        PROCESSING,
        SUCCESS,
        FAILURE
    }
}
