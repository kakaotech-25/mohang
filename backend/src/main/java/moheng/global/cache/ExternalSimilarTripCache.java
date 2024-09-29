package moheng.global.cache;

import org.springframework.cache.concurrent.ConcurrentMapCache;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ExternalSimilarTripCache extends ConcurrentMapCache {
    private final Map<Object, LocalDateTime> tripCache = new ConcurrentHashMap<>();
    private final long expiresDatePoint;

    public ExternalSimilarTripCache(final String name, final long expiresDatePoint) {
        super(name);
        this.expiresDatePoint = expiresDatePoint;
    }

    @Override
    protected Object lookup(final Object key) {
        LocalDateTime expiredDate = tripCache.get(key);
        if(Objects.isNull(expiredDate) || isCacheValid(expiredDate)) {
            return super.lookup(key);
        }

        tripCache.remove(key);
        super.evict(key);
        return null;
    }

    @Override
    public void put(final Object key, final Object value) {
        LocalDateTime expiredAt = LocalDateTime.now().plusSeconds(expiresDatePoint);
        tripCache.put(key, expiredAt);

        super.put(key, value);
    }

    private boolean isCacheValid(final LocalDateTime expiredDate) {
        return LocalDateTime.now().isBefore(expiredDate);
    }
}
