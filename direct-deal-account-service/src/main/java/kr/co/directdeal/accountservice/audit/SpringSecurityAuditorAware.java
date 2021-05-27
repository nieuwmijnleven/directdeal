package kr.co.directdeal.accountservice.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import kr.co.directdeal.accountservice.config.Constants;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder
                                            .getContext()
                                            .getAuthentication();

        Optional<String> principal = Optional.empty();
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            principal = Optional.ofNullable(springSecurityUser.getUsername());
        } else if (authentication.getPrincipal() instanceof String) {
            principal = Optional.ofNullable((String)authentication.getPrincipal());
        }

        return Optional.of(principal.orElse(Constants.SYSTEM_ACCOUNT)
                                    .replace("anonymousUser", Constants.SYSTEM_ACCOUNT));
    }
}
