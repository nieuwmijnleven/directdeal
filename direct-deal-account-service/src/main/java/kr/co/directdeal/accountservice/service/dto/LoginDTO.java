package kr.co.directdeal.accountservice.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

   @NotNull
   @Size(min = 5, max = 64)
   private String email;

   @NotNull
   @Size(min = 6, max = 30, message = "{account.constraint.password.size.message}")
   private String password;
}
