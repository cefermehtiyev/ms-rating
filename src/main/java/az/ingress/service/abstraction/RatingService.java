package az.ingress.service.abstraction;

import az.ingress.model.request.RatingRequest;
import az.ingress.model.response.RatingResponse;

public interface RatingService {
    void insertOrUpdateRating(RatingRequest ratingRequest);

    void deleteRating(Long productId, Long userId);

    RatingResponse getUserRating(Long productId, Long userId);
}
