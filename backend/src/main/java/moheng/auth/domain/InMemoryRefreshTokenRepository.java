package moheng.auth.domain;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryRefreshTokenRepository {
    private static final Map<Long, String> repository = new ConcurrentHashMap<>();

    public void save(final long memberId, final String refreshToken) {
        repository.put(memberId, refreshToken);
    }

    public boolean existsById(final long memberId) {
        return repository.containsKey(memberId);
    }

    public String findById(long memberId) {
        String refreshToken = repository.get(memberId);
        return refreshToken;
    }
}
