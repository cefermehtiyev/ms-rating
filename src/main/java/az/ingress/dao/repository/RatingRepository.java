package az.ingress.dao.repository;

import az.ingress.dao.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<RatingEntity, Long> {
    Optional<RatingEntity> findByProductIdAndUserId(Long productId, Long userId);
}
