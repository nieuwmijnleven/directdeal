package kr.co.directdeal.accountservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.accountservice.domain.account.Account;
import kr.co.directdeal.accountservice.exception.AccountException;
import kr.co.directdeal.accountservice.service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AccountDetailService implements UserDetailsService {

   private final AccountRepository accountRepository;

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String email) {
      return accountRepository.findOneWithAuthoritiesByEmail(email)
               .filter(Account::isActivated)
               .map(this::createUser)
               .orElseThrow(() -> AccountException.builder()
                  .messageKey("accountdetailservice.exception.loaduserbyusername.accountnotfound.message")
                  .messageArgs(new String[]{email})
                  .build());
   }

   private User createUser(Account account) {
      List<GrantedAuthority> grantedAuthorities = account.getAuthorities().stream()
              .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
              .collect(Collectors.toList());
      return new User(account.getEmail(), account.getPassword(), grantedAuthorities);
   }
}
