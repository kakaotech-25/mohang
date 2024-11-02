package moheng.config.slice;

import moheng.config.TestConfig;
import moheng.global.config.JpaAuditConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(JpaAuditConfig.class)
@ActiveProfiles("test")
public abstract class RepositoryTestConfig {
}
