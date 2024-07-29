package moheng.auth.domain;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryRefreshTokenRepository implements RefreshTokenRepository {
    private static final Map<Long, String> repository = new ConcurrentHashMap<>();

    @Override
    public void save(final long memberId, final String refreshToken) {
        repository.put(memberId, refreshToken);
    }

    @Override
    public boolean existsById(final long memberId) {
        return repository.containsKey(memberId);
    }

    @Override
    public String findById(final long memberId) {
        String refreshToken = repository.get(memberId);
        return refreshToken;
    }
}
