package samryong.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import samryong.account.domain.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findById(long id);
}
