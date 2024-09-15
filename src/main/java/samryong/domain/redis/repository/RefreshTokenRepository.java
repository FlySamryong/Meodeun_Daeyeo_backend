package samryong.domain.redis.repository;

import org.springframework.data.repository.CrudRepository;
import samryong.domain.redis.entity.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {}
