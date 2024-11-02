package moheng.runner.warmup;

import moheng.keyword.application.KeywordService;
import moheng.keyword.dto.TripsByKeyWordsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile({"dev", "prod"})
@Order(7)
@Component
public class KeywordWarmUpRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(KeywordWarmUpRunner.class);
    private final KeywordService keywordService;

    public KeywordWarmUpRunner(final KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            keywordService.findRecommendTripsByKeywords(new TripsByKeyWordsRequest(List.of(1L)));
            keywordService.findRecommendTripsByRandomKeyword();
        } catch (Exception e) {
            log.info("Latency 개선을 위한 JVM Warm Up 처리중입니다.");
        }
    }
}
