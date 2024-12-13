package az.ingress.schedular;

import az.ingress.dao.repository.RatingCacheOutboxRepository;
import az.ingress.service.abstraction.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RatingCacheOutboxScheduler {
    private final RatingCacheOutboxRepository ratingCacheOutboxRepository;
    private final CacheService cacheService;

    @Scheduled(cron = "5 * * * * * ")
    @SchedulerLock(name = "processOutboxEntries",lockAtLeastFor = "PT1M",lockAtMostFor = "PT3M")
    @Transactional
    public void processOutboxEntries() {
        var unprocessedEntries = ratingCacheOutboxRepository.findByProcessedFalse();
        unprocessedEntries.forEach(
                entry -> {
                    try {
                        cacheService.save(entry.getProductId(), entry.getVoteCount(), entry.getAverageRating());
                        entry.setProcessed(true);
                        ratingCacheOutboxRepository.save(entry);

                        log.info("Outbox entry successfully processed and marked. ID: {}", entry.getId());
                    } catch (Exception e) {
                        log.error("Error occurred while processing outbox entry. ID: {} - Will retry.", entry.getId(), e);
                    }
                }
        );

    }
}
