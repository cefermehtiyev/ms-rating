package az.ingress.mapper

import az.ingress.model.dto.CacheDto
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

class RatingCacheOutboxMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "BuildOutboxEntityTest"(){
        given:
        def productId = random.nextObject(Long)
        def cacheDto = random.nextObject(CacheDto)

        when:
        def response = RatingCacheOutboxMapper.RATING_CACHE_OUTBOX_MAPPER.buildOutBoxEntity(productId,cacheDto)

        then:
        response.productId == productId
        response.voteCount == cacheDto.voteCount
        response.averageRating == cacheDto.averageRating
        response.processed == false
    }
}
