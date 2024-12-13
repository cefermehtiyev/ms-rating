package az.ingress.mapper;

import az.ingress.dao.entity.RatingDetailsEntity;

import java.math.BigDecimal;

public enum RatingDetailsMapper {
    RATING_DETAILS_MAPPER;

    public RatingDetailsEntity buildRatingDetails(Long productId, Integer voteCount, BigDecimal averageRating) {
        return RatingDetailsEntity.builder()
                .productId(productId)
                .voteCount(voteCount)
                .averageRating(averageRating)
                .build();
    }

    public void updateRatingDetails(RatingDetailsEntity ratingDetailsEntity, Integer voteCount, BigDecimal averageRating) {
        ratingDetailsEntity.setVoteCount(voteCount);
        ratingDetailsEntity.setAverageRating(averageRating);
    }

}
