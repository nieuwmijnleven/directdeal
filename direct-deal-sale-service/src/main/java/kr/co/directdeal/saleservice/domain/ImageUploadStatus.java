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
    private String id;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CreatedDate
    @Column
    private Instant createdDate; 

    public enum Status {
        PROCESSING,
        SUCCESS,
        FAILURE
    }
}
