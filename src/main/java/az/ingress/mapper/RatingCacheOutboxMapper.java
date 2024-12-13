package az.ingress.mapper;

import az.ingress.dao.entity.RatingCacheOutboxEntity;
import az.ingress.model.dto.CacheDto;

public enum RatingCacheOutboxMapper {
    RATING_CACHE_OUTBOX_MAPPER;

    public RatingCacheOutboxEntity buildOutBoxEntity(Long productId, CacheDto cacheDto) {
        return RatingCacheOutboxEntity.builder()
                .productId(productId)
                .voteCount(cacheDto.getVoteCount())
                .averageRating(cacheDto.getAverageRating())
                .processed(false)
                .build();
    }
}
