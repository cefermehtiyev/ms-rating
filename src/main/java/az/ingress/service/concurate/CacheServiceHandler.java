package az.ingress.service.concurate;


import az.ingress.annotation.RedisLock;
import az.ingress.dao.repository.RatingCacheOutboxRepository;

import az.ingress.dao.repository.RatingDetailsRepository;
import az.ingress.mapper.CacheDtoMapper;
import az.ingress.mapper.RatingCacheOutboxMapper;
import az.ingress.model.dto.CacheDto;
import az.ingress.service.abstraction.CacheService;
import az.ingress.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;


import static az.ingress.mapper.CacheDtoMapper.CACHE_DTO_MAPPER;
import static az.ingress.model.constants.CacheKey.CACHE_KEY;

@Slf4j
@RequiredArgsConstructor
@Service
public class CacheServiceHandler implements CacheService {
    private final CacheUtil cacheUtil;
    private final RatingCacheOutboxRepository ratingCacheOutboxRepository;
    private final RatingDetailsRepository ratingDetailsRepository;


    @Override
    @RedisLock(key = "'lock:product-id:' + #productId", waitTime = 10, leaseTime = 5)
    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    public void save(Long productId, Integer voteCount, BigDecimal averageRating) {
        var cacheKey = CACHE_KEY + productId;
        var cacheDto = CACHE_DTO_MAPPER.buildCacheDto(voteCount, averageRating);
        try {

            cacheUtil.saveToCache(cacheKey, cacheDto, 1L, ChronoUnit.DAYS);
            log.info("Data save to Cache");

        } catch (Exception ex) {
            log.info("Data save to Cache Out Box");
            var outBox = RatingCacheOutboxMapper
                    .RATING_CACHE_OUTBOX_MAPPER.buildOutBoxEntity(productId, cacheDto);

            ratingCacheOutboxRepository.save(outBox);
        }

    }


    public CacheDto get(Long productId) {
        try {
            var cacheKey = CACHE_KEY + productId;
            var cacheData = cacheUtil.getBucket(cacheKey);
            log.info("ActionLog.cacheData:{}", cacheData);
            return (CacheDto) cacheData;
        } catch (Exception ex) {
            var ratingDetails = ratingDetailsRepository.findByProductId(productId).get();
            return CACHE_DTO_MAPPER.buildCacheDto(ratingDetails.getVoteCount(), ratingDetails.getAverageRating());
        }


    }


}
