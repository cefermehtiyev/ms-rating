package az.ingress.mapper

import az.ingress.dao.entity.RatingEntity
import az.ingress.model.request.RatingRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

class RatingMapperTest extends Specification {

    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()


    def "BuildRatingEntityTest"() {
        given:
        def request = random.nextObject(RatingRequest)

        when:
        def entity = RatingMapper.RATING_MAPPER.buiildRatingEntity(request)

        then:
        request.userId == entity.userId
        request.productId == request.productId
        request.score == entity.score
    }

    def "BuildRatingResponseTest"(){
        given:
        def entity = random.nextObject(RatingEntity)

        when:
        def response  = RatingMapper.RATING_MAPPER.buildRatingResponse(entity)

        then:
        entity.id == response.id
        entity.userId == response.userId
        entity.productId == response.productId
        entity.score == response.score
        entity.status == response.status

    }

    def "UpdateRatingEntityTest"() {
        given:
        def request = random.nextObject(RatingRequest)
        def entity = random.nextObject(RatingEntity)

        when:
        RatingMapper.RATING_MAPPER.updateRatingEntity(entity, request)

        then:
        entity.score == request.score

    }

    def "BuildRatingDtoTest"(){
        given:
        def entity = random.nextObject(RatingEntity)

        when:
        def response = RatingMapper.RATING_MAPPER.buildRatingDto(entity)

        then:
        entity.userId == response.userId
        entity.productId == response.productId
        entity.score == response.score
        entity.status == response.status
        entity.createdAt == response.createdAt
        entity.updatedAt == response.updatedAt
    }

}
