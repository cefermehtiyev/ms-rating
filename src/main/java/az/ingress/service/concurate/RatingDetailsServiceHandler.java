package az.ingress.service.concurate;

import az.ingress.dao.entity.RatingDetailsEntity;
import az.ingress.dao.repository.RatingDetailsRepository;

import az.ingress.model.dto.CacheDto;
import az.ingress.model.dto.RatingDto;
import az.ingress.model.queue.RatingQueueDto;
import az.ingress.queue.QueueSender;
import az.ingress.service.abstraction.CacheService;
import az.ingress.service.abstraction.RatingDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static az.ingress.mapper.RatingDetailsMapper.RATING_DETAILS_MAPPER;
import static az.ingress.mapper.factory.RatingQueueFactory.RATING_QUEUE_FACTORY;
import static az.ingress.model.enums.RatingStatus.DELETED;

@Slf4j
@RequiredArgsConstructor
@Service
public class RatingDetailsServiceHandler implements RatingDetailsService {

    private final RatingDetailsRepository ratingDetailsRepository;
    private final CacheService cacheService;
    private final QueueSender<RatingQueueDto> ratingQueueDtoQueueSender;


    private void insertOrUpdateRatingDetails(Long productId, Integer voteCount, BigDecimal averageRating) {
        var ratingDetails = ratingDetailsRepository.findByProductId(productId)
                .map(existingRatingDetails -> updateExistingRatingDetails(existingRatingDetails, voteCount, averageRating))
                .orElseGet(() -> RATING_DETAILS_MAPPER.buildRatingDetails(productId, voteCount, averageRating));
        ratingDetailsRepository.save(ratingDetails);
        var ratingQueueDto = RATING_QUEUE_FACTORY.buildRatingQueue(productId, voteCount, averageRating);
        ratingQueueDtoQueueSender.sendToQueue(ratingQueueDto);

    }


    @Async
    public void updateRatingDetails(RatingDto ratingDto) {
        var cacheData = cacheService.get(ratingDto.getProductId());
        int updatedVoteCount;
        BigDecimal averageRating;

        if (cacheData != null) {
            log.info("Data Retrieved From cache:{}", ratingDto.getProductId());

            if (ratingDto.getCreatedAt().equals(ratingDto.getUpdatedAt())) {
                log.info("No Rating details found for productId:{} and userId:{}", ratingDto.getProductId(), ratingDto.getUserId());
                updatedVoteCount = cacheData.getVoteCount() + 1;
                averageRating = calculateAverageRating(cacheData, ratingDto.getScore(), updatedVoteCount);
            } else if (ratingDto.getStatus().equals(DELETED)) {
                log.info("Rating Deleted from Rating details");
                updatedVoteCount = cacheData.getVoteCount() - 1;
                averageRating = calculateAverageRating(cacheData, ratingDto.getScore(), updatedVoteCount);
            } else {
                log.info("Ratings found for productId:{} and userId :{}", ratingDto.getProductId(), ratingDto.getUserId());
                updatedVoteCount = cacheData.getVoteCount();
                averageRating = calculateAverageRating(cacheData, ratingDto.getScore(), updatedVoteCount);
                log.info("UpdatedAverage :{}", averageRating);
            }
        } else {
            log.info("Cache miss for product ID:{}", ratingDto.getProductId());
            updatedVoteCount = 1;
            averageRating = BigDecimal.valueOf(ratingDto.getScore());
        }

        insertOrUpdateRatingDetails(ratingDto.getProductId(), updatedVoteCount, averageRating);
        cacheService.save(ratingDto.getProductId(), updatedVoteCount, averageRating);
        log.info("Completed updateProductRatingSummary on thread: {}", Thread.currentThread().getName());


    }

    private BigDecimal calculateAverageRating(CacheDto cacheData, Integer newRating, Integer updatedRatingCount) {
        return cacheData.getAverageRating()
                .multiply(BigDecimal.valueOf(cacheData.getVoteCount()))
                .add(BigDecimal.valueOf(newRating)).divide(BigDecimal.valueOf(updatedRatingCount), 1, RoundingMode.UP);
    }


    private RatingDetailsEntity updateExistingRatingDetails(RatingDetailsEntity existingRatingDetails, Integer voteCount, BigDecimal averageRating) {
        RATING_DETAILS_MAPPER.updateRatingDetails(existingRatingDetails, voteCount, averageRating);
        return existingRatingDetails;
    }


}



