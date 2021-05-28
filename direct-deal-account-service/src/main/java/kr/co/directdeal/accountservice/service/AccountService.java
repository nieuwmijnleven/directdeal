package kr.co.directdeal.accountservice.service;

import kr.co.directdeal.accountservice.service.dto.AccountDTO;
import kr.co.directdeal.accountservice.service.dto.PasswordDTO;

public interface AccountService {
	
	public AccountDTO createAccount(AccountDTO accountDTO);
	
	public AccountDTO updateAccount(AccountDTO accountDTO);
	
	public AccountDTO getAccount(String id);
	
	public void deleteAccount(String id);

	public void changePassword(PasswordDTO passwordDTO);

}
