package kr.co.directdeal.accountservice.port.outbound;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.accountservice.domain.object.Account;

/**
 * Repository interface for Account entity persistence operations.
 *
 * <p>Extends JpaRepository to provide basic CRUD operations.</p>
 *
 * <p>Provides custom query methods to find accounts by email,
 * including loading associated authorities eagerly.</p>
 *
 * Author: Cheol Jeon
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Finds an Account by its email.
     *
     * @param email the email to search for
     * @return an Optional containing the found Account, or empty if not found
     */
    Optional<Account> findByEmail(String email);

    /**
     * Finds an Account by email and fetches associated authorities eagerly.
     *
     * @param email the email to search for
     * @return an Optional containing the found Account with authorities loaded, or empty if not found
     */
    @EntityGraph(attributePaths = "authorities")
    Optional<Account> findOneWithAuthoritiesByEmail(String email);
}
