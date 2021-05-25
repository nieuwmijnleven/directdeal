package kr.co.directdeal.accountservice.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.accountservice.domain.account.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    public Optional<Account> findByEmail(String email);
}
