package az.ingress.dao.repository;

import az.ingress.dao.entity.RatingCacheOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingCacheOutboxRepository extends JpaRepository<RatingCacheOutboxEntity, Long> {
    List<RatingCacheOutboxEntity> findByProcessedFalse();
}
