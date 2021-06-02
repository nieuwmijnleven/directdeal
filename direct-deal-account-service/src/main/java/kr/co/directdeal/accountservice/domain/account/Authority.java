package kr.co.directdeal.accountservice.domain.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import kr.co.directdeal.accountservice.config.constants.AuthorityConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "authority")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Authority {

	public static final Authority ADMIN = new Authority(AuthorityConstants.ADMIN);

	public static final Authority USER = new Authority(AuthorityConstants.USER);

	@Id	
	@Column(name = "authority_name")
	private String authorityName;
}
