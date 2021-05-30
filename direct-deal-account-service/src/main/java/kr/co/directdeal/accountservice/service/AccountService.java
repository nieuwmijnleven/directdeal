package kr.co.directdeal.accountservice.service;

import kr.co.directdeal.accountservice.service.dto.AccountDTO;
import kr.co.directdeal.accountservice.service.dto.PasswordDTO;

public interface AccountService {
	
	public AccountDTO createAccount(AccountDTO accountDTO);
	
	public AccountDTO updateAccount(AccountDTO accountDTO);

	public AccountDTO updateAccountById(AccountDTO accountDTO);
	
	public AccountDTO getAccount(String email);
	
	public void deleteAccount(String email);

	public void changePassword(String email, PasswordDTO passwordDTO);

}
