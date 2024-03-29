package kr.co.directdeal.accountservice.domain.account;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kr.co.directdeal.accountservice.service.dto.AccountDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "ACCOUNT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class Account extends AbstractAuditingEntity {
	@JsonIgnore
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ACCOUNT_ID")
	private Long id;
	
	@Email
	@NotNull
    @Size(min = 5, max = 64)
    @Column(name = "ACCOUNT_EMAIL", length = 64, unique = true)
	private String email;
	
	@JsonIgnore
	@NotNull
	@Size(min = 60, max = 60)
	@Column(name = "ACCOUNT_PASSWORD_HASH", length = 60, nullable = false)
	private String password;
	
	@NotNull
	@Size(min = 1, max = 30)
	@Column(name = "ACCOUNT_NAME", length = 30)
	private String name;

	@NotNull
	@Column(name = "ACCOUNT_ACTIVATED", nullable = false)
	private boolean activated;

	@ManyToMany
	@JoinTable(
		name = "ACCOUNT_AUTHORITY",
		joinColumns = {@JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ACCOUNT_ID")},
		inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_NAME", referencedColumnName = "AUTHORITY_NAME")})
	private Set<Authority> authorities;
	
	public void updateFrom(AccountDTO dto) {
		// if (Objects.nonNull(dto.getEmail())) this.email = dto.getEmail();
		if (Objects.nonNull(dto.getName())) this.name = dto.getName();
	}
	
	public void changePassword(String encodedNewPassword) {
		this.password = encodedNewPassword;
	}

	public void active() {
		this.activated = true;
	}

	public void inactive() {
		this.activated = false;
	}
}
