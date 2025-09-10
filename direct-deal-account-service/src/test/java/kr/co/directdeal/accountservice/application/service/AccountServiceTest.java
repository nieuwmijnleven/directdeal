package kr.co.directdeal.accountservice.application.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import kr.co.directdeal.accountservice.port.inbound.AccountUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.co.directdeal.accountservice.domain.object.Account;
import kr.co.directdeal.accountservice.exception.AccountException;
import kr.co.directdeal.accountservice.application.service.dto.AccountDTO;
import kr.co.directdeal.accountservice.application.service.dto.PasswordDTO;
import kr.co.directdeal.accountservice.application.service.mapper.AccountMapper;
import kr.co.directdeal.accountservice.application.service.mapper.Mapper;
import kr.co.directdeal.accountservice.port.outbound.AccountRepository;

@ExtendWith(MockitoExtension.class) 
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    private AccountUseCase accountUseCase;

    private Mapper<Account, AccountDTO> mapper = new AccountMapper();

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void init() {
        this.accountUseCase = new AccountService(
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
        accountUseCase.createAccount(accountDTO);

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
            accountUseCase.createAccount(accountDTO);
        });
    }

    @Test
    public void UpdateAccount_IncorrectEmail_ThrowAccountException() {
        //given
        AccountDTO accountDTO = mock(AccountDTO.class);
        given(accountDTO.getEmail())
            .willReturn("account@directdeal.co.kr");

        given(accountRepository.findByEmail(accountDTO.getEmail()))
            .willReturn(Optional.empty());
       
        //when and then
        assertThrows(AccountException.class, () -> {
            accountUseCase.updateAccount(accountDTO);
        });
    }

    @Test
    public void UpdateAccountById_CorrectIdAndUniqueEmail_Updated() {
        //given
        AccountDTO accountDTO = mock(AccountDTO.class);
        given(accountDTO.getId())
            .willReturn(1L);
        given(accountDTO.getEmail())
            .willReturn("user@directdeal.co.kr");

        Account accountById = mock(Account.class);
        given(accountById.getId())
            .willReturn(1L);

        given(accountRepository.findById(accountDTO.getId()))
            .willReturn(Optional.of(accountById));
        given(accountRepository.findByEmail(accountDTO.getEmail()))
            .willReturn(Optional.empty());

        //when
        accountUseCase.updateAccountById(accountDTO);

        //then
        verify(accountRepository).findById(accountDTO.getId());
        verify(accountRepository).findByEmail(accountDTO.getEmail());
        verify(accountById).updateFrom(accountDTO);
    }

    @Test
    public void UpdateAccountById_IncorrectEmail_ThrowAccountException() {
        //given
        AccountDTO accountDTO = mock(AccountDTO.class);
        given(accountDTO.getId())
            .willReturn(1L);

        given(accountRepository.findById(accountDTO.getId()))
            .willReturn(Optional.empty());
       
        //when and then
        assertThrows(AccountException.class, () -> {
            accountUseCase.updateAccountById(accountDTO);
        });
    }

    @Test
    public void UpdateAccountById_SameInfo_Updated() {
        //given
        AccountDTO accountDTO = mock(AccountDTO.class);
        given(accountDTO.getId())
            .willReturn(1L);
        given(accountDTO.getEmail())
            .willReturn("account@directdeal.co.kr");

        Account accountById = mock(Account.class);
        given(accountById.getId())
            .willReturn(1L);
        given(accountDTO.getEmail())
            .willReturn("account@directdeal.co.kr");

        Account accountByEmail = mock(Account.class);
        given(accountByEmail.getId())
            .willReturn(1L);

        given(accountRepository.findById(accountDTO.getId()))
            .willReturn(Optional.of(accountById));
        given(accountRepository.findByEmail(accountDTO.getEmail()))
            .willReturn(Optional.of(accountByEmail));

        //when
        accountUseCase.updateAccountById(accountDTO);

        //then
        verify(accountRepository).findById(accountDTO.getId());
        verify(accountRepository).findByEmail(accountDTO.getEmail());
        verify(accountById).updateFrom(accountDTO);
    }

    @Test
    public void UpdateAccountById_NonUniqueEmail_Updated() {
        //given
        AccountDTO accountDTO = mock(AccountDTO.class);
        given(accountDTO.getId())
            .willReturn(1L);
        given(accountDTO.getEmail())
            .willReturn("account@directdeal.co.kr");

        Account accountById = mock(Account.class);
        given(accountById.getId())
            .willReturn(1L);
        given(accountDTO.getEmail())
            .willReturn("account@directdeal.co.kr");

        Account accountByEmail = mock(Account.class);
        given(accountByEmail.getId())
            .willReturn(2L);

        given(accountRepository.findById(accountDTO.getId()))
            .willReturn(Optional.of(accountById));
        given(accountRepository.findByEmail(accountDTO.getEmail()))
            .willReturn(Optional.of(accountByEmail));

        //when and then
        assertThrows(AccountException.class, () -> {
            accountUseCase.updateAccountById(accountDTO);
        });
    }

    @Test
    public void GetAccount_CorrectId_ReturnAccountDTO() {
        //given
        String loginEmail = "account@directdeal.co.kr";

        Account accountByEmail = mock(Account.class);
        given(accountByEmail.getId())
            .willReturn(1L);
        given(accountByEmail.getEmail())
            .willReturn(loginEmail);
        given(accountByEmail.getPassword())
            .willReturn("$2a$10$9arZnwNlbgpxMHdLv82ZXuOLID5ODJR0BhciQ1wxvds2ei1hzG8he");
        given(accountByEmail.getName())
            .willReturn("account");
        given(accountByEmail.isActivated())
            .willReturn(true);

        given(accountRepository.findByEmail(loginEmail))
            .willReturn(Optional.of(accountByEmail));
        
        //when
        AccountDTO resultDTO = accountUseCase.getAccount(loginEmail);

        //then
        verify(accountByEmail).getId();
        verify(accountByEmail).getEmail();
        verify(accountByEmail).getPassword();
        verify(accountByEmail).getName();
        verify(accountByEmail).isActivated();

        assertThat(resultDTO.getId(), equalTo(accountByEmail.getId()));
        assertThat(resultDTO.getEmail(), equalTo(accountByEmail.getEmail()));
        assertThat(resultDTO.getPassword(), equalTo(accountByEmail.getPassword()));
        assertThat(resultDTO.getName(), equalTo(accountByEmail.getName()));
        assertThat(resultDTO.isActivated(), equalTo(accountByEmail.isActivated()));
    }

    @Test
    public void GetAccount_IncorrectId_ThrowAccountException() {
        //given
        String loginEmail = "account@directdeal.co.kr";

        given(accountRepository.findByEmail(loginEmail))
            .willReturn(Optional.empty());
        
        //when and then
        assertThrows(AccountException.class, () -> {
            accountUseCase.getAccount(loginEmail);
        });
    }

    @Test
    public void DeleteAccount_CorrectId_Deleted() {
        //given
        String loginEmail = "account@directdeal.co.kr";

        given(accountRepository.findByEmail(loginEmail))
            .willReturn(Optional.of(mock(Account.class)));
        
        //when
        accountUseCase.deleteAccount(loginEmail);

        //then
        verify(accountRepository).delete(any(Account.class));
    }

    @Test
    public void DeleteAccount_CorrectId_ThrowAccountException() {
        //given
        String loginEmail = "account@directdeal.co.kr";

        given(accountRepository.findByEmail(loginEmail))
            .willReturn(Optional.empty());
        
        //when and then
        assertThrows(AccountException.class, () -> {
            accountUseCase.deleteAccount(loginEmail);
        });
    }

    @Test
    public void ChangePassword_SamePasswords_ThrowAccountException() {
        //given
        PasswordDTO passwordDTO = PasswordDTO.builder()
                                        .password("1q2w3e")
                                        .newPassword("1q2w3e")
                                        .build();
        
        String loginEmail = "account@directdeal.co.kr";

        //when and then
        assertThrows(AccountException.class, () -> {
            accountUseCase.changePassword(loginEmail, passwordDTO);
        });
    }

    @Test
    public void ChangePassword_IncorrectEmail_ThrowAccountException() {
        //given
        PasswordDTO passwordDTO = PasswordDTO.builder()
                                        .password("1q2w3e")
                                        .newPassword("123qwe")
                                        .build();

        String loginEmail = "account@directdeal.co.kr";
        given(accountRepository.findByEmail(loginEmail))
            .willReturn(Optional.empty());
        
        //when and then
        assertThrows(AccountException.class, () -> {
            accountUseCase.changePassword(loginEmail, passwordDTO);
        });
    }

    @Test
    public void ChangePassword_IncorrectPassword_ThrowAccountException() {
        //given
        PasswordDTO passwordDTO = PasswordDTO.builder()
                                        .password("-1q2w3e")
                                        .newPassword("123qwe")
                                        .build();

        Account accountByEmail = Account.builder()
                                .password("$2a$10$9arZnwNlbgpxMHdLv82ZXuOLID5ODJR0BhciQ1wxvds2ei1hzG8he")
                                .build();
        
        String loginEmail = "account@directdeal.co.kr";
        given(accountRepository.findByEmail(loginEmail))
            .willReturn(Optional.of(accountByEmail));
        
        //when and then
        assertThrows(AccountException.class, () -> {
            accountUseCase.changePassword(loginEmail, passwordDTO);
        });
    }

    @Test
    public void ChangePassword_NotSamePasswordAndCorrectIdAndCorrectPassword_Changed() {
        //given
        PasswordDTO passwordDTO = PasswordDTO.builder()
                                        .password("1q2w3e")
                                        .newPassword("123qwe")
                                        .build();

        Account accountByEmail = mock(Account.class);
        given(accountByEmail.getPassword())
            .willReturn("$2a$10$9arZnwNlbgpxMHdLv82ZXuOLID5ODJR0BhciQ1wxvds2ei1hzG8he");

        String loginEmail = "account@directdeal.co.kr";
        given(accountRepository.findByEmail(loginEmail))
            .willReturn(Optional.of(accountByEmail));
        
        //when
        accountUseCase.changePassword(loginEmail, passwordDTO);

        //then
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(accountByEmail).changePassword(argument.capture());
        assertThat(passwordEncoder.matches(passwordDTO.getNewPassword(), argument.getValue()), equalTo(true));
    }
}   
