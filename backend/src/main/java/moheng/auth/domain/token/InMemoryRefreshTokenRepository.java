package moheng.auth.domain.token;

import org.springframework.stereotype.Component;

import java.util.Map;
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

    @Override
    public void deleteAll() {
        repository.clear();
    }

    @Override
    public long deleteById(final long memberId) {
        repository.remove(memberId);
        return memberId;
    }
}
