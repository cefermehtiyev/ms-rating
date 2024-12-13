package az.ingress.service

import az.ingress.dao.entity.RatingEntity
import az.ingress.dao.repository.RatingRepository
import az.ingress.model.enums.RatingStatus
import az.ingress.model.request.RatingRequest
import az.ingress.service.abstraction.RatingDetailsService
import az.ingress.service.concurate.RatingServiceHandler
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import java.time.LocalDateTime

import static az.ingress.mapper.RatingMapper.RATING_MAPPER

class RatingServiceHandlerTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    RatingRepository ratingRepository
    RatingDetailsService ratingDetailsService
    RatingServiceHandler ratingServiceHandler

    def setup(){
        ratingRepository = Mock()
        ratingDetailsService = Mock()
        ratingServiceHandler = new RatingServiceHandler(ratingRepository,ratingDetailsService)
    }

    def "InsertOrUpdateTest rating insert case"(){
        given:
        def ratingRequest = random.nextObject(RatingRequest, "score")
        ratingRequest.score = random.nextInt(1, 5)
        def ratingEntity = RATING_MAPPER.buiildRatingEntity(ratingRequest)

        when:
        ratingServiceHandler.insertOrUpdateRating(ratingRequest)
        then:
        1 * ratingRepository.findByProductIdAndUserId(ratingRequest.productId,ratingRequest.userId) >> Optional.empty()
        1 * ratingRepository.save(ratingEntity)

    }

    def "InsertOrUpdateTest rating updated case"(){
        given:
        def ratingRequest = random.nextObject(RatingRequest, "score")
        ratingRequest.score = random.nextInt(1, 5)
        def ratingEntity = random.nextObject(RatingEntity)
        ratingEntity.setUserId(ratingRequest.userId)
        ratingEntity.setProductId(ratingRequest.productId)
        ratingEntity.setScore(4)


        when:
        ratingServiceHandler.insertOrUpdateRating(ratingRequest)
        RATING_MAPPER.updateRatingEntity(ratingEntity, ratingRequest)


        then:
        1 * ratingRepository.findByProductIdAndUserId(ratingRequest.productId,ratingRequest.userId) >> Optional.of(ratingEntity)
        1 * ratingRepository.save(ratingEntity)

    }



}
