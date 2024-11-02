package moheng.config.slice;

import moheng.auth.domain.token.RefreshTokenRepository;
import moheng.config.DatabaseCleaner;
import moheng.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles("test")
public abstract class ServiceTestConfig {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    public void setUp() {
        refreshTokenRepository.deleteAll();
        databaseCleaner.clear();
    }
}
