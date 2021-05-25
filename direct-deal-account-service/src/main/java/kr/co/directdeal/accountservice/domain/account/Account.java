package kr.co.directdeal.accountservice.domain.account;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import kr.co.directdeal.accountservice.service.dto.AccountDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class Account extends AbstractAuditingEntity {
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	@Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
	private String email;
	
	@Size(min = 60, max = 60)
	@Column(name = "password_hash", length = 60, nullable = false)
	private String password;
	
	@Size(min = 1, max = 30)
	@Column(length = 30)
	private String name;
	
	public void updateFrom(AccountDTO dto) {
		if (Objects.nonNull(dto.getEmail())) this.email = dto.getEmail();
		if (Objects.nonNull(dto.getName())) this.name = dto.getName();
	}
	
	public void updatePassword(String newPassword) {
		this.password = newPassword;
	}
}
