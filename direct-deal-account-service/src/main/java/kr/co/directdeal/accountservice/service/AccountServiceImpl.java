package kr.co.directdeal.accountservice.service;

import java.util.Collections;
import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.accountservice.domain.account.Account;
import kr.co.directdeal.accountservice.domain.account.Authority;
import kr.co.directdeal.accountservice.exception.AccountException;
import kr.co.directdeal.accountservice.service.dto.AccountDTO;
import kr.co.directdeal.accountservice.service.dto.PasswordDTO;
import kr.co.directdeal.accountservice.service.mapper.Mapper;
import kr.co.directdeal.accountservice.service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
	
	private final AccountRepository accountRepository;
	
	private final Mapper<Account, AccountDTO> mapper;
	
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public AccountDTO createAccount(AccountDTO accountDTO) {
		boolean unusedEmail = accountRepository
								.findByEmail(accountDTO.getEmail())
								.map(accountByEmail -> Boolean.FALSE)
								.orElse(Boolean.TRUE);
		if (!unusedEmail) 
			throw AccountException.builder()
					.messageKey("account.exception.insert.email.message")
					.messageArgs(new String[]{accountDTO.getEmail()})
					.build();

		
		Account newAccount = Account.builder()
								.id(accountDTO.getId())
								.email(accountDTO.getEmail())
								.password(passwordEncoder.encode(accountDTO.getPassword()))
								.name(accountDTO.getName())
								.activated(true)
								.authorities(Collections.singleton(new Authority("role_user")))
								.build();		

		return mapper.toDTO(accountRepository.save(newAccount));
	}
	
	@Override
	@Transactional
	public AccountDTO updateAccount(AccountDTO accountDTO) {	
		Account accountByEmail = accountRepository
								.findByEmail(accountDTO.getEmail())
								.orElseThrow(() -> AccountException.builder()
										.messageKey("account.exception.update.id.message")
										.messageArgs(new String[]{accountDTO.getEmail()})
										.build());
		
		accountByEmail.updateFrom(accountDTO);
		return mapper.toDTO(accountByEmail);
	}

	public AccountDTO updateAccountById(AccountDTO accountDTO) {	
		Account accountById = accountRepository
								.findById(accountDTO.getId())
								.orElseThrow(() -> AccountException.builder()
										.messageKey("account.exception.update.id.message")
										.messageArgs(new String[]{accountDTO.getId()})
										.build());
			
		boolean validEmail = accountRepository
								.findByEmail(accountDTO.getEmail())
								.filter(accountByEmail -> !Objects.equals(accountById.getId(), accountByEmail.getId()))
								.map(accountByEmail -> Boolean.FALSE)
								.orElse(Boolean.TRUE);
		if (!validEmail) 
			throw AccountException.builder()
					.messageKey("account.exception.update.email.message")
					.messageArgs(new String[]{accountDTO.getEmail()})
					.build();

		accountById.updateFrom(accountDTO);
		return mapper.toDTO(accountById);
	}
	
	@Override
	@Transactional(readOnly = true)
	public AccountDTO getAccount(String email) {
		Account account = accountRepository
							.findByEmail(email)
							.orElseThrow(() -> AccountException.builder()
								.messageKey("account.exception.get.message")
								.messageArgs(new String[]{email})
								.build());

		return mapper.toDTO(account);
	}
	
	@Override
	@Transactional
	public void deleteAccount(String email) {
		Account account = accountRepository
							.findByEmail(email)
							.orElseThrow(() -> AccountException.builder()
								.messageKey("account.exception.delete.message")
								.messageArgs(new String[]{email})
								.build());

		accountRepository.delete(account);
	}

	@Override
	@Transactional
	public void changePassword(String email, PasswordDTO passwordDTO) {
		if (passwordDTO.isSamePasswords()) {
			throw AccountException.builder()
				.messageKey("account.exception.changepassword.samepasswords.message")
				.build();
		}

		Account account = accountRepository
							.findByEmail(email)
							.orElseThrow(() -> AccountException.builder()
								.messageKey("account.exception.changepassword.accountnotfound.message")
								.messageArgs(new String[]{email})
								.build());
		
		String encodedPassword = account.getPassword();
		if (!passwordEncoder.matches(passwordDTO.getPassword(), encodedPassword)) {
			throw AccountException.builder()
					.messageKey("account.exception.changepassword.passwordmismatch.message")
					.build();
		}

		String encodedNewPassword = passwordEncoder.encode(passwordDTO.getNewPassword());
		account.changePassword(encodedNewPassword);
	}
}
