package moheng.config;

import moheng.auth.domain.token.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public abstract class ServiceTestConfig {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    public void setUp() {
        databaseCleaner.execute();
        refreshTokenRepository.deleteAll();
    }
}
