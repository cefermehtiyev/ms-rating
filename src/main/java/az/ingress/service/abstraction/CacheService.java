package az.ingress.service.abstraction;

import az.ingress.model.dto.CacheDto;

import java.math.BigDecimal;

public interface CacheService {
    void save(Long productId, Integer voteCount, BigDecimal averageRating);

    CacheDto get(Long productId);

}
