package kr.co.directdeal.common.security.util;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import kr.co.directdeal.common.security.constants.Constants;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityUtils {
    
    public static String getCurrentUserLogin() {
        Authentication authentication = SecurityContextHolder
                                            .getContext()
                                            .getAuthentication();
        if (authentication == null) {
            return Constants.SYSTEM_ACCOUNT;
        }

        Optional<String> principal = Optional.empty();
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            principal = Optional.ofNullable(springSecurityUser.getUsername());
        } else if (authentication.getPrincipal() instanceof String) {
            principal = Optional.ofNullable((String)authentication.getPrincipal());
        }

        return principal.orElse(Constants.SYSTEM_ACCOUNT)
                        .replace("anonymousUser", Constants.SYSTEM_ACCOUNT);
    }   
}
