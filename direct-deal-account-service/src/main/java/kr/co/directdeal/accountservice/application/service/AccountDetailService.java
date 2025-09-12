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

/**
 * Service that implements {@link UserDetailsService} to load user-specific data.
 *
 * <p>This service loads {@link UserDetails} by the user's email from the Account repository.
 * It ensures the account is activated before returning the user details, including granted authorities.</p>
 *
 * @author Cheol Jeon
 */
@RequiredArgsConstructor
@Component
public class AccountDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;

    /**
     * Locates the user based on the email.
     *
     * <p>Loads the account along with its authorities, checks if activated, and maps it to a Spring Security {@link User}.</p>
     *
     * @param email the email identifying the user whose data is required.
     * @return a fully populated {@link UserDetails} object (never {@code null})
     * @throws UsernameNotFoundException if the user could not be found or is not activated
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) {
        return accountRepository.findOneWithAuthoritiesByEmail(email)
                .filter(Account::isActivated)
                .map(this::createUser)
                .orElseThrow(() -> new UsernameNotFoundException("Could not find the user with email: " + email));
    }

    /**
     * Converts the domain {@link Account} to a Spring Security {@link User} instance,
     * mapping authorities accordingly.
     *
     * @param account the domain account object
     * @return a Spring Security UserDetails instance with granted authorities
     */
    private User createUser(Account account) {
        List<GrantedAuthority> grantedAuthorities = account.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());
        return new User(account.getEmail(), account.getPassword(), grantedAuthorities);
    }
}
