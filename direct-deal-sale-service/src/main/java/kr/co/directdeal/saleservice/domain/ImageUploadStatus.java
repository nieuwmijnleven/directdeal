package kr.co.directdeal.saleservice.domain;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
    @Column(name = "IMAGE_UPLOAD_STATUS_ID")
    private String id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "IMAGE_UPLOAD_STATUS_RESULT")
    private Status status;

    @CreatedDate
    @Column(name = "IMAGE_UPLOAD_STATUS_CREATED_DATE")
    private Instant createdDate; 

    public enum Status {
        PROCESSING,
        SUCCESS,
        FAILURE
    }
}
