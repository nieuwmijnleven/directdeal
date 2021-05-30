package kr.co.directdeal.accountservice.adapter.inbound;

import javax.validation.Valid;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.accountservice.audit.SecurityUtils;
import kr.co.directdeal.accountservice.service.AccountService;
import kr.co.directdeal.accountservice.service.dto.AccountDTO;
import kr.co.directdeal.accountservice.service.dto.PasswordDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {
	
	private final AccountService accountService;
	
	@RequestMapping(method = RequestMethod.OPTIONS)
	public ResponseEntity<?> options() {
		return ResponseEntity
				.ok()
				.allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, 
						HttpMethod.DELETE, HttpMethod.OPTIONS)
				.build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void createAccount(@Valid @RequestBody AccountDTO accountDTO) {
		accountService.createAccount(accountDTO);
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void updateAccount(@Valid @RequestBody AccountDTO accountDTO) {
		accountDTO.setEmail(SecurityUtils.getCurrentUserLogin());
		accountService.updateAccount(accountDTO);
	}
	
	@GetMapping
	public AccountDTO getAccount() {
		String loginEmail = SecurityUtils.getCurrentUserLogin();
		return accountService.getAccount(loginEmail);
	}
	
	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAccount() {
		String loginEmail = SecurityUtils.getCurrentUserLogin();
		accountService.deleteAccount(loginEmail);
	}

	@PutMapping("/change-password")
	@ResponseStatus(HttpStatus.OK)
	public void changePassword(@Valid @RequestBody PasswordDTO passwordDTO) {	
		String loginEmail = SecurityUtils.getCurrentUserLogin();
		log.debug("loginEmail => " + loginEmail);
		accountService.changePassword(loginEmail, passwordDTO);
	}
}
