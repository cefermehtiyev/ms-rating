package az.ingress.mapper.factory;

import az.ingress.model.queue.RatingQueueDto;

import java.math.BigDecimal;

public enum RatingQueueFactory {
    RATING_QUEUE_FACTORY;

    public RatingQueueDto buildRatingQueue(Long productId,Integer voteCount, BigDecimal averageRating) {
        return RatingQueueDto.builder()
                .productId(productId)
                .voteCount(voteCount)
                .averageRating(averageRating)
                .build();
    }
}
