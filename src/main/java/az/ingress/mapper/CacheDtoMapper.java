package az.ingress.mapper;

import az.ingress.model.dto.CacheDto;

import java.math.BigDecimal;

public enum CacheDtoMapper {
    CACHE_DTO_MAPPER;

    public CacheDto buildCacheDto(Integer voteCount, BigDecimal averageRating) {
        return CacheDto.builder()
                .voteCount(voteCount)
                .averageRating(averageRating)
                .build();
    }
}
