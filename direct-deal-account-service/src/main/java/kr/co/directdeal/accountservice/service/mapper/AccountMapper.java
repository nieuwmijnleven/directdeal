package kr.co.directdeal.accountservice.service.mapper;

import org.springframework.stereotype.Component;

import kr.co.directdeal.accountservice.domain.account.Account;
import kr.co.directdeal.accountservice.service.dto.AccountDTO;

@Component
public class AccountMapper implements Mapper<Account, AccountDTO> {

	@Override
	public Account toEntity(AccountDTO dto) {
		return Account.builder()
					.id(dto.getId())
					.email(dto.getEmail())
					.password(dto.getPassword())
					.name(dto.getName())
					.build();
	}

	@Override
	public AccountDTO toDTO(Account entity) {
		return AccountDTO.builder()
					.id(entity.getId())
					.email(entity.getEmail())
					//.password(entity.getPassword())
					.name(entity.getName())
					.createdDate(entity.getCreatedDate())
					.lastModifiedDate(entity.getLastModifiedDate())
					.build();
	}
}
