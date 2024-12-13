package az.ingress.mapper

import az.ingress.model.dto.CacheDto
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

class CacheDtoMapperTest extends Specification{
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "BuildCacheDtoTest"(){
        given:
        def voteCount = random.nextObject(Integer)
        def averageRating = random.nextObject(BigDecimal)

        when:
        def response = CacheDtoMapper.CACHE_DTO_MAPPER.buildCacheDto(voteCount,averageRating)

        then:
        response.voteCount == voteCount
        response.averageRating == averageRating


    }
}
