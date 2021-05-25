package kr.co.directdeal.accountservice.service;

import kr.co.directdeal.accountservice.service.dto.AccountDTO;
import kr.co.directdeal.accountservice.service.dto.PasswordDTO;

public interface AccountService {
	
	public AccountDTO createAccount(AccountDTO accountDTO);
	
	public AccountDTO updateAccount(AccountDTO accountDTO);
	
	public AccountDTO getAccount(AccountDTO accountDTO);
	
	public void deleteAccount(AccountDTO accountDTO);

	public void changePassword(PasswordDTO PasswordDTO);

}
