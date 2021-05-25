package kr.co.directdeal.accountservice.service.dto;

import java.time.Instant;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AccountDTO {
	
	private String id;

	@Email
    @Size(min = 5, max = 254)
	private String email;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Size(min = 6, max = 30, message = "{account.constraint.password.size.message}")
	//@Size(min = 6, max = 30)
	private String password;

	@Size(min = 1, max = 30, message = "{account.constraint.name.size.message}")
	private String name;
	
	@JsonIgnore
	private String createdBy;

    private Instant createdDate;

	@JsonIgnore
	private String lastModifiedBy;

    private Instant lastModifiedDate;
}
