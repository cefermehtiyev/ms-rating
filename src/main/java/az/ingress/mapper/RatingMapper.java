package az.ingress.mapper;

import az.ingress.dao.entity.RatingEntity;
import az.ingress.model.dto.RatingDto;
import az.ingress.model.request.RatingRequest;
import az.ingress.model.response.RatingResponse;

import static az.ingress.model.enums.RatingStatus.ACTIVE;

public enum RatingMapper {
    RATING_MAPPER;

    public RatingEntity buiildRatingEntity(RatingRequest ratingRequest) {
        return RatingEntity.builder()
                .userId(ratingRequest.getUserId())
                .productId(ratingRequest.getProductId())
                .score(ratingRequest.getScore())
                .status(ACTIVE)
                .build();
    }

    public RatingResponse buildRatingResponse(RatingEntity ratingEntity) {
        return RatingResponse.builder()
                .id(ratingEntity.getId())
                .userId(ratingEntity.getUserId())
                .productId(ratingEntity.getProductId())
                .score(ratingEntity.getScore())
                .status(ratingEntity.getStatus())
                .build();
    }

    public void updateRatingEntity(RatingEntity ratingEntity, RatingRequest ratingRequest) {
        ratingEntity.setScore(ratingRequest.getScore());
    }

    public RatingDto buildRatingDto(RatingEntity ratingEntity){
        return RatingDto.builder()
                .userId(ratingEntity.getUserId())
                .productId(ratingEntity.getProductId())
                .score(ratingEntity.getScore())
                .status(ratingEntity.getStatus())
                .createdAt(ratingEntity.getCreatedAt())
                .updatedAt(ratingEntity.getUpdatedAt())
                .build();
    }
}
