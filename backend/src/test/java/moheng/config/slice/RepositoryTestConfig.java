package moheng.config.slice;

import moheng.global.config.JpaAuditConfig;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import(JpaAuditConfig.class)
@ActiveProfiles("test")
public abstract class RepositoryTestConfig {
}
