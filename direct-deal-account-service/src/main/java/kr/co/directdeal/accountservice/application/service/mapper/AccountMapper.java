package kr.co.directdeal.accountservice.application.service.mapper;

import org.springframework.stereotype.Component;

import kr.co.directdeal.accountservice.domain.object.Account;
import kr.co.directdeal.accountservice.application.service.dto.AccountDTO;

/**
 * Mapper component to convert between Account entity and AccountDTO.
 *
 * Responsible for mapping fields between the domain object and the data transfer object.
 */
@Component
public class AccountMapper implements Mapper<Account, AccountDTO> {

    /**
     * Converts an AccountDTO to an Account entity.
     *
     * Note: Password and activated fields are not set here to prevent accidental overrides.
     *
     * @param dto the AccountDTO to convert
     * @return the Account entity
     */
    @Override
    public Account toEntity(AccountDTO dto) {
        return Account.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                //.password(dto.getPassword()) // intentionally commented out to avoid overwriting
                .name(dto.getName())
                //.activated(dto.isActivated()) // intentionally commented out
                .build();
    }

    /**
     * Converts an Account entity to an AccountDTO.
     *
     * @param entity the Account entity to convert
     * @return the AccountDTO
     */
    @Override
    public AccountDTO toDTO(Account entity) {
        return AccountDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .name(entity.getName())
                .createdDate(entity.getCreatedDate())
                .lastModifiedDate(entity.getLastModifiedDate())
                .activated(entity.isActivated())
                .build();
    }
}
