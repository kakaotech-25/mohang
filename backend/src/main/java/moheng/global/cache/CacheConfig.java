package moheng.global.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {
    public static final String EXTERNAL_SIMILAR_TRIP_CACHE = "SIMILAR_TRIP_CACHE";
    private static final long LIFE_CYCLE = 60 * 60 * 2;

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(List.of(new ExternalConcurrentMapSimilarTripCache(EXTERNAL_SIMILAR_TRIP_CACHE, LIFE_CYCLE)));
        return simpleCacheManager;
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void evict() {
        ExternalConcurrentMapSimilarTripCache externalConcurrentMapSimilarTripCache = (ExternalConcurrentMapSimilarTripCache)  cacheManager().getCache(EXTERNAL_SIMILAR_TRIP_CACHE);
        externalConcurrentMapSimilarTripCache.evictAllTrips();
    }
}
