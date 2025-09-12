package kr.co.directdeal.common.security.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import kr.co.directdeal.common.security.util.SecurityUtils;

/**
 * AuditorAware implementation that retrieves the current auditor (user)
 * from the Spring Security context.
 *
 * Returns the currently authenticated user's login as the auditor.
 * If no user is authenticated, returns an empty Optional.
 *
 * @author Cheol Jeon
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityUtils.getCurrentUserLogin());
    }
}
