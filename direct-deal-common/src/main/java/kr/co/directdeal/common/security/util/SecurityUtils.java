package kr.co.directdeal.common.security.util;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import kr.co.directdeal.common.security.constants.Constants;
import lombok.experimental.UtilityClass;

/**
 * Utility class for Spring Security related helper methods.
 */
@UtilityClass
public class SecurityUtils {

    /**
     * Retrieves the login name of the currently authenticated user.
     * If no user is authenticated, returns a system account identifier.
     *
     * @return the username of the current user, or a system account name if none.
     */
    public static String getCurrentUserLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Constants.SYSTEM_ACCOUNT;
        }

        Optional<String> principal = Optional.empty();
        if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            principal = Optional.ofNullable(springSecurityUser.getUsername());
        } else if (authentication.getPrincipal() instanceof String) {
            principal = Optional.ofNullable((String) authentication.getPrincipal());
        }

        return principal.orElse(Constants.SYSTEM_ACCOUNT)
                .replace("anonymousUser", Constants.SYSTEM_ACCOUNT);
    }
}
