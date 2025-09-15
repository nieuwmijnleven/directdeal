package kr.co.directdeal.accountservice.domain.object;

import java.io.Serializable;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

/**
 * Base class for entities which will hold auditing information.
 *
 * <p>This class is annotated as a JPA mapped superclass and uses
 * Spring Data JPA auditing annotations to automatically populate
 * the created and last modified user and timestamps.</p>
 *
 * <p>Fields included are:</p>
 * <ul>
 *   <li>createdBy - the user who created the entity</li>
 *   <li>createdDate - the timestamp when the entity was created</li>
 *   <li>lastModifiedBy - the user who last modified the entity</li>
 *   <li>lastModifiedDate - the timestamp when the entity was last modified</li>
 * </ul>
 *
 * <p>Author: Cheol Jeon</p>
 */
@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @CreatedBy
    @Column(name = "CREATED_BY", nullable = false, length = 50, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "CREATED_DATE", updatable = false)
    private final Instant createdDate = Instant.now();

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY", nullable = false, length = 50)
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    private final Instant lastModifiedDate = Instant.now();
}
