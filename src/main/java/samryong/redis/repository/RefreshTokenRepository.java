package samryong.redis.repository;

import org.springframework.data.repository.CrudRepository;
import samryong.redis.domain.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {}
