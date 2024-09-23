package moheng.applicationrunner.warmup.member;

import moheng.keyword.application.KeywordService;
import moheng.keyword.dto.TripsByKeyWordsRequest;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeywordWarmUpRunner implements ApplicationRunner {
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

        }
    }
}
