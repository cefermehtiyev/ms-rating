package az.ingress.dao.repository;

import az.ingress.dao.entity.RatingDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatingDetailsRepository extends JpaRepository<RatingDetailsEntity, Long> {
    Optional<RatingDetailsEntity> findByProductId(Long productId);
}
