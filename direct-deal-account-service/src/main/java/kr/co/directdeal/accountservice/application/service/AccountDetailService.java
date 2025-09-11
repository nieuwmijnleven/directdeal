package kr.co.directdeal.accountservice.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.accountservice.domain.object.Account;
import kr.co.directdeal.accountservice.port.outbound.AccountRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AccountDetailService implements UserDetailsService {

   private final AccountRepository accountRepository;

   @Override
   @Transactional(readOnly = true)
   public UserDetails loadUserByUsername(String email) {
      return accountRepository.findOneWithAuthoritiesByEmail(email)
               .filter(Account::isActivated)
               .map(this::createUser)
               .orElseThrow(() -> new UsernameNotFoundException("could not find the user."));
   }

   private User createUser(Account account) {
      List<GrantedAuthority> grantedAuthorities = account.getAuthorities().stream()
              .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
              .collect(Collectors.toList());
      return new User(account.getEmail(), account.getPassword(), grantedAuthorities);
   }
}
