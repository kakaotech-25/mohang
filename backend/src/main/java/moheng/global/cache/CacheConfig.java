package moheng.global.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    public static final String EXTERNAL_AI_CACHE = "aiCache";
}
