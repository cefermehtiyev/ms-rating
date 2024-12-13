package az.ingress.service.concurate;

import az.ingress.dao.entity.RatingEntity;
import az.ingress.dao.repository.RatingRepository;
import az.ingress.exception.NotFoundException;
import az.ingress.model.request.RatingRequest;
import az.ingress.model.response.RatingResponse;
import az.ingress.service.abstraction.RatingDetailsService;
import az.ingress.service.abstraction.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static az.ingress.exception.ErrorMessage.RATING_NOT_FOUND;
import static az.ingress.mapper.RatingMapper.RATING_MAPPER;
import static az.ingress.model.enums.RatingStatus.DELETED;

@Slf4j
@RequiredArgsConstructor
@Service
public class RatingServiceHandler implements RatingService {

    private final RatingRepository ratingRepository;
    private final RatingDetailsService ratingDetailsService;

    @Override
    public void insertOrUpdateRating(RatingRequest ratingRequest) {
        var ratingEntityOptional = ratingRepository.findByProductIdAndUserId(ratingRequest.getProductId(), ratingRequest.getUserId());
        ratingEntityOptional.ifPresent(ratingEntity -> ratingRequest.setScore(ratingRequest.getScore() - ratingEntity.getScore()));
        var ratingEntity = ratingEntityOptional
                .map(existingRating -> updateExistingRating(existingRating, ratingRequest))
                .orElseGet(() -> RATING_MAPPER.buiildRatingEntity(ratingRequest));
        ratingRepository.save(ratingEntity);
        ratingDetailsService.updateRatingDetails(RATING_MAPPER.buildRatingDto(ratingEntity));
    }

    private RatingEntity updateExistingRating(RatingEntity existingRating, RatingRequest ratingRequest) {
        RATING_MAPPER.updateRatingEntity(existingRating, ratingRequest);
        return existingRating;
    }

    @Override
    public void deleteRating(Long productId, Long userId) {
        var rating = fetchRatingExist(productId, userId);
        rating.setStatus(DELETED);
        ratingRepository.save(rating);
        rating.setScore(-rating.getScore());
        ratingDetailsService.updateRatingDetails(RATING_MAPPER.buildRatingDto(rating));
    }

    @Override
    public RatingResponse getUserRating(Long productId, Long userId) {
        return RATING_MAPPER.buildRatingResponse(fetchRatingExist(productId, userId));
    }


    public RatingEntity fetchRatingExist(Long productId, Long userId) {
        return ratingRepository.findByProductIdAndUserId(productId, userId).orElseThrow(
                () -> new NotFoundException(RATING_NOT_FOUND.getMessage())
        );
    }


}