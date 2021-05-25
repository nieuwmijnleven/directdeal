package kr.co.directdeal.accountservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.accountservice.domain.account.Account;
import kr.co.directdeal.accountservice.exception.AccountException;
import kr.co.directdeal.accountservice.service.dto.AccountDTO;
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

		String hashedPassword = passwordEncoder.encode(accountDTO.getPassword());
		Account newAccount = mapper.toEntity(accountDTO);
		newAccount.updatePassword(hashedPassword);
		return mapper.toDTO(accountRepository.save(newAccount));
	}
	
	@Override
	@Transactional
	public AccountDTO updateAccount(AccountDTO accountDTO) {	
		Account accountById = accountRepository
								.findById(accountDTO.getId())
								.orElseThrow(() -> AccountException.builder()
										.messageKey("account.exception.update.id.message")
										.messageArgs(new String[]{accountDTO.getId()})
										.build());
			
		boolean validEmail = accountRepository
								.findByEmail(accountDTO.getEmail())
								.filter(accountByEmail -> accountById.getId() != accountByEmail.getId())
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
	public AccountDTO getAccount(AccountDTO accountDTO) {
		Account account = accountRepository
							.findById(accountDTO.getId())
							.orElseThrow(() -> AccountException.builder()
								.messageKey("account.exception.get.message")
								.messageArgs(new String[]{accountDTO.getId()})
								.build());

		return mapper.toDTO(account);
	}
	
	@Override
	public void deleteAccount(AccountDTO accountDTO) {
		accountRepository
			.findById(accountDTO.getId())
			.map(account -> {
				accountRepository.delete(account);
				return account;})
			.orElseThrow(() -> AccountException.builder()
				.messageKey("account.exception.delete.message")
				.messageArgs(new String[]{accountDTO.getId()})
				.build());
	}
}
