package samryong.account.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import samryong.account.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findById(long id);
}
