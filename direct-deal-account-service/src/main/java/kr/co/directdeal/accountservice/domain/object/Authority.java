package kr.co.directdeal.accountservice.domain.object;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import kr.co.directdeal.common.security.constants.AuthorityConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity class representing an Authority.
 *
 * <p>This entity defines the roles or authorities assigned to accounts, such as ADMIN or USER.</p>
 *
 * <p>Two static constants are provided for common roles: ADMIN and USER.</p>
 *
 * <p>Author: Cheol Jeon</p>
 */
@Entity
@Table(name = "AUTHORITY")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Authority {

    /**
     * Predefined ADMIN authority constant.
     */
    public static final Authority ADMIN = new Authority(AuthorityConstants.ADMIN);

    /**
     * Predefined USER authority constant.
     */
    public static final Authority USER = new Authority(AuthorityConstants.USER);

    /**
     * Name of the authority (role).
     */
    @NotNull
    @Size(min = 1, max = 30)
    @Id
    @Column(name = "AUTHORITY_NAME", length = 30)
    private String authorityName;
}
