package kr.co.directdeal.accountservice.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.co.directdeal.accountservice.domain.account.Account;
import kr.co.directdeal.accountservice.exception.AccountException;
import kr.co.directdeal.accountservice.service.dto.AccountDTO;
import kr.co.directdeal.accountservice.service.mapper.AccountMapper;
import kr.co.directdeal.accountservice.service.mapper.Mapper;
import kr.co.directdeal.accountservice.service.repository.AccountRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

// @TestInstance(Lifecycle.PER_CLASS)
// @ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    private AccountService accountService;

    private Mapper<Account, AccountDTO> mapper = new AccountMapper();

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void init() {
        this.accountService = new AccountServiceImpl(
                                    accountRepository, 
                                    mapper, 
                                    passwordEncoder);
    }

    @Test
    public void CreateAccount_UniqueEmail_Created() {
        //given
        AccountDTO accountDTO = AccountDTO.builder()
                                    .email("account@directdeal.co.kr")
                                    .password("plainPassword")
                                    .name("account")
                                    .build();

        given(accountRepository.findByEmail(accountDTO.getEmail()))
            .willReturn(Optional.empty());

        given(accountRepository.save(any(Account.class)))
            .willReturn(mock(Account.class));

        //when
        accountService.createAccount(accountDTO);

        //then
        verify(accountRepository).findByEmail(accountDTO.getEmail());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    public void CreateAccount_DuplicateEmail_ThrowAccountException() {
        //given
        AccountDTO accountDTO = AccountDTO.builder()
                                    .email("account@directdeal.co.kr")
                                    .password("plainPassword")
                                    .name("account")
                                    .build();

        given(accountRepository.findByEmail(accountDTO.getEmail()))
            .willReturn(Optional.of(mock(Account.class)));
    
        //when and then
        assertThrows(AccountException.class, () -> {
            accountService.createAccount(accountDTO);
        });
    }

    @Test
    public void UpdateAccount_CorrectIdAndUniqueEmail_Updated() {
        //given
        AccountDTO accountDTO = mock(AccountDTO.class);
        given(accountDTO.getId())
            .willReturn("1");
        given(accountDTO.getEmail())
            .willReturn("user@directdeal.co.kr");

        Account accountById = mock(Account.class);
        given(accountById.getId())
            .willReturn("1");

        given(accountRepository.findById(accountDTO.getId()))
            .willReturn(Optional.of(accountById));
        given(accountRepository.findByEmail(accountDTO.getEmail()))
            .willReturn(Optional.empty());

        //when
        accountService.updateAccount(accountDTO);

        //then
        verify(accountRepository).findById(accountDTO.getId());
        verify(accountRepository).findByEmail(accountDTO.getEmail());
        verify(accountById).updateFrom(accountDTO);
    }

    @Test
    public void UpdateAccount_IncorrectId_ThrowAccountException() {
        //given
        AccountDTO accountDTO = mock(AccountDTO.class);
        given(accountDTO.getId())
            .willReturn("1");

        given(accountRepository.findById(accountDTO.getId()))
            .willReturn(Optional.empty());
       
        //when and then
        assertThrows(AccountException.class, () -> {
            accountService.updateAccount(accountDTO);
        });
    }

    @Test
    public void UpdateAccount_SameInfo_Updated() {
        //given
        AccountDTO accountDTO = mock(AccountDTO.class);
        given(accountDTO.getId())
            .willReturn("1");
        given(accountDTO.getEmail())
            .willReturn("account@directdeal.co.kr");

        Account accountById = mock(Account.class);
        given(accountById.getId())
            .willReturn("1");
        given(accountDTO.getEmail())
            .willReturn("account@directdeal.co.kr");

        Account accountByEmail = mock(Account.class);
        given(accountByEmail.getId())
            .willReturn("1");

        given(accountRepository.findById(accountDTO.getId()))
            .willReturn(Optional.of(accountById));
        given(accountRepository.findByEmail(accountDTO.getEmail()))
            .willReturn(Optional.of(accountByEmail));

        //when
        accountService.updateAccount(accountDTO);

        //then
        verify(accountRepository).findById(accountDTO.getId());
        verify(accountRepository).findByEmail(accountDTO.getEmail());
        verify(accountById).updateFrom(accountDTO);
    }

    @Test
    public void UpdateAccount_NonUniqueEmail_Updated() {
        //given
        AccountDTO accountDTO = mock(AccountDTO.class);
        given(accountDTO.getId())
            .willReturn("1");
        given(accountDTO.getEmail())
            .willReturn("account@directdeal.co.kr");

        Account accountById = mock(Account.class);
        given(accountById.getId())
            .willReturn("1");
        given(accountDTO.getEmail())
            .willReturn("account@directdeal.co.kr");

        Account accountByEmail = mock(Account.class);
        given(accountByEmail.getId())
            .willReturn("2");

        given(accountRepository.findById(accountDTO.getId()))
            .willReturn(Optional.of(accountById));
        given(accountRepository.findByEmail(accountDTO.getEmail()))
            .willReturn(Optional.of(accountByEmail));

        //when and then
        assertThrows(AccountException.class, () -> {
            accountService.updateAccount(accountDTO);
        });
    }

    @Test
    public void GetAccount_CorrectId_ReturnAccountDTO() {
        //given
        String accountId = "1";

        Account accountById = mock(Account.class);
        given(accountById.getId())
            .willReturn(accountId);
        given(accountById.getEmail())
            .willReturn("account@directdeal.co.kr");
        given(accountById.getPassword())
            .willReturn("$2a$10$9arZnwNlbgpxMHdLv82ZXuOLID5ODJR0BhciQ1wxvds2ei1hzG8he");
        given(accountById.getName())
            .willReturn("account");
        given(accountById.isActivated())
            .willReturn(true);

        given(accountRepository.findById(accountId))
            .willReturn(Optional.of(accountById));
        
        //when
        AccountDTO resultDTO = accountService.getAccount(accountId);

        //then
        verify(accountById).getId();
        verify(accountById).getEmail();
        verify(accountById).getPassword();
        verify(accountById).getName();
        verify(accountById).isActivated();

        assertThat(resultDTO.getId(), equalTo(accountById.getId()));
        assertThat(resultDTO.getEmail(), equalTo(accountById.getEmail()));
        assertThat(resultDTO.getPassword(), equalTo(accountById.getPassword()));
        assertThat(resultDTO.getName(), equalTo(accountById.getName()));
        assertThat(resultDTO.isActivated(), equalTo(accountById.isActivated()));
    }

    @Test
    public void GetAccount_IncorrectId_ThrowAccountException() {
        //given
        String accountId = "1";

        given(accountRepository.findById(accountId))
            .willReturn(Optional.empty());
        
        //when and then
        assertThrows(AccountException.class, () -> {
            accountService.getAccount(accountId);
        });
    }

    @Test
    public void DeleteAccount_CorrectId_Deleted() {
        //given
        String accountId = "1";

        given(accountRepository.findById(accountId))
            .willReturn(Optional.of(mock(Account.class)));
        
        //when
        accountService.deleteAccount(accountId);

        //then
        verify(accountRepository).delete(any(Account.class));
    }

    @Test
    public void DeleteAccount_CorrectId_ThrowAccountException() {
        //given
        String accountId = "1";

        given(accountRepository.findById(accountId))
            .willReturn(Optional.empty());
        
        //when and then
        assertThrows(AccountException.class, () -> {
            accountService.deleteAccount(accountId);
        });
    }
}   
