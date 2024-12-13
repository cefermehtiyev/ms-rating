package az.ingress.service

import az.ingress.dao.entity.RatingDetailsEntity
import az.ingress.dao.repository.RatingDetailsRepository
import az.ingress.mapper.CacheDtoMapper
import az.ingress.mapper.RatingDetailsMapper
import az.ingress.model.dto.CacheDto
import az.ingress.model.dto.RatingDto
import az.ingress.model.enums.RatingStatus
import az.ingress.model.queue.RatingQueueDto
import az.ingress.queue.QueueSender
import az.ingress.service.abstraction.CacheService
import az.ingress.service.concurate.RatingDetailsServiceHandler
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import java.math.RoundingMode
import java.time.LocalDateTime

import static az.ingress.mapper.RatingDetailsMapper.RATING_DETAILS_MAPPER
import static az.ingress.model.enums.RatingStatus.DELETED

class RatingDetailsServiceHandlerTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    RatingDetailsServiceHandler ratingDetailsServiceHandler
    RatingDetailsRepository ratingDetailsRepository
    CacheService cacheService
    QueueSender<RatingQueueDto> ratingQueueDtoQueueSender

    def setup() {
        ratingDetailsRepository = Mock()
        cacheService = Mock()
        ratingQueueDtoQueueSender = Mock()
        ratingDetailsServiceHandler = new RatingDetailsServiceHandler(ratingDetailsRepository, cacheService, ratingQueueDtoQueueSender)

    }

    def "UpdatedRatingDetailsTest cache miss"() {
        given:
        def ratingDto = random.nextObject(RatingDto, "score")
        ratingDto.score = random.nextInt(1, 5)

        def updatedRatingCount = 1
        def averageRating = BigDecimal.valueOf(ratingDto.getScore())
        def ratingDetails = RATING_DETAILS_MAPPER.buildRatingDetails(ratingDto.productId, updatedRatingCount, averageRating)


        when:
        ratingDetailsServiceHandler.updateRatingDetails(ratingDto)


        then:
        1 * cacheService.get(ratingDto.productId) >> null
        1 * ratingDetailsRepository.findByProductId(ratingDto.productId) >> Optional.empty()
        1 * ratingDetailsRepository.save(ratingDetails)


    }

    def "UpdatedRatingDetailsTest cache hit and createdAt and updatedAt equals"() {
        given:
        def ratingDto = random.nextObject(RatingDto, "score")
        ratingDto.score = random.nextInt(1, 5)
        ratingDto.setCreatedAt(LocalDateTime.of(2024, 11, 20, 13, 12, 11, 11))
        ratingDto.setUpdatedAt(LocalDateTime.of(2024, 11, 20, 13, 12, 11, 11))
        def cacheData = new CacheDto(4, 2.0)
        def updatedVoteCount = cacheData.voteCount + 1
        def ratingDetailsEntity = new RatingDetailsEntity(ratingDto.productId, 4, 2.0)
        def averageRating = cacheData.getAverageRating()
                .multiply(BigDecimal.valueOf(cacheData.getVoteCount()))
                .add(BigDecimal.valueOf(ratingDto.score)).divide(BigDecimal.valueOf(updatedVoteCount), 1, RoundingMode.UP)
        ratingDetailsEntity.setVoteCount(updatedVoteCount)
        ratingDetailsEntity.setAverageRating(averageRating)


        when:
        ratingDetailsServiceHandler.updateRatingDetails(ratingDto)

        then:
        1 * cacheService.get(ratingDto.productId) >> cacheData
        1 * ratingDetailsRepository.findByProductId(ratingDto.productId) >> Optional.of(ratingDetailsEntity)
        1 * ratingDetailsRepository.save(ratingDetailsEntity)

    }

    def "UpdatedRatingDetailsTest cache hit and status equals DELETED"() {
        given:
        def ratingDto = random.nextObject(RatingDto, "score")
        ratingDto.score = random.nextInt(1, 5)
        ratingDto.setStatus(DELETED)
        def cacheData = new CacheDto(3, 4.0)
        def updatedVoteCount = cacheData.voteCount - 1
        def averageRating = cacheData.getAverageRating()
                .multiply(BigDecimal.valueOf(cacheData.getVoteCount()))
                .add(BigDecimal.valueOf(ratingDto.score)).divide(BigDecimal.valueOf(updatedVoteCount), 1, RoundingMode.UP)
        def ratingDetailsEntity = new RatingDetailsEntity(ratingDto.productId, 3, 4.0)
        ratingDetailsEntity.setVoteCount(updatedVoteCount)
        ratingDetailsEntity.setAverageRating(averageRating)


        when:
        ratingDetailsServiceHandler.updateRatingDetails(ratingDto)

        then:
        1 * cacheService.get(ratingDto.productId) >> cacheData
        1 * ratingDetailsRepository.findByProductId(ratingDto.productId) >> Optional.of(ratingDetailsEntity)
        1 * ratingDetailsRepository.save(ratingDetailsEntity)


    }

    def "UpdatedRatingDetailsTest cache hit and createdAt and updatedAt not equals" (){
        given:
        def ratingDto = random.nextObject(RatingDto,"score")
        ratingDto.score = random.nextInt(1,5)
        def cacheData = new CacheDto(2,4.0)
        def updatedVoteCount = cacheData.voteCount
        def averageRating = cacheData.getAverageRating()
                .multiply(BigDecimal.valueOf(cacheData.getVoteCount()))
                .add(BigDecimal.valueOf(ratingDto.score)).divide(BigDecimal.valueOf(updatedVoteCount), 1, RoundingMode.UP)
        def ratingDetailsEntity = new RatingDetailsEntity(ratingDto.productId,3,3.0)
        ratingDetailsEntity.setVoteCount(updatedVoteCount)
        ratingDetailsEntity.setAverageRating(averageRating)


        when:
        ratingDetailsServiceHandler.updateRatingDetails(ratingDto)

        then:
        1 * cacheService.get(ratingDto.productId) >> cacheData
        1 * ratingDetailsRepository.findByProductId(ratingDto.productId) >> Optional.of(ratingDetailsEntity)
        1 * ratingDetailsRepository.save(ratingDetailsEntity)

    }

}
