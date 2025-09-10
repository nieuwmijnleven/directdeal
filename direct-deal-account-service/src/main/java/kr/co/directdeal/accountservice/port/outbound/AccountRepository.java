package kr.co.directdeal.accountservice.port.outbound;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.accountservice.domain.object.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    public Optional<Account> findByEmail(String email);

    @EntityGraph(attributePaths = "authorities")
    public Optional<Account> findOneWithAuthoritiesByEmail(String email);
}
