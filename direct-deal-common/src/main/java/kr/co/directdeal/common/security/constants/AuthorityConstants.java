package kr.co.directdeal.common.security.constants;

import lombok.experimental.UtilityClass;

/**
 * Constants defining authority roles used throughout the security module.
 */
@UtilityClass
public class AuthorityConstants {

    /**
     * Role granted to administrators.
     */
    public static final String ADMIN = "ROLE_ADMIN";

    /**
     * Role granted to regular authenticated users.
     */
    public static final String USER = "ROLE_USER";

    /**
     * Role granted to anonymous (unauthenticated) users.
     */
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
}
