package kr.co.directdeal.accountservice.domain.account;

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

@Entity
@Table(name = "AUTHORITY")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Authority {

	public static final Authority ADMIN = new Authority(AuthorityConstants.ADMIN);

	public static final Authority USER = new Authority(AuthorityConstants.USER);

	@NotNull
	@Size(min = 1, max = 30)
	@Id	
	@Column(name = "AUTHORITY_NAME", length = 30)
	private String authorityName;
}
