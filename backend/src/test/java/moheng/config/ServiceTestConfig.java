package moheng.config;

import moheng.auth.domain.token.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public abstract class ServiceTestConfig {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void setUp() {
        refreshTokenRepository.deleteAll();
    }
}
