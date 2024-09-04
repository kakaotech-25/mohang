package moheng.applicationrunner.dev;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import moheng.applicationrunner.dto.KeywordRunner;
import moheng.applicationrunner.dto.LiveInformationRunner;
import moheng.keyword.domain.KeywordRepository;
import moheng.keyword.domain.TripKeywordRepository;
import moheng.trip.domain.TripRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(11)
@Component
public class KeywordDevApplicationRunner implements ApplicationRunner {
    private final KeywordRepository keywordRepository;
    private final TripRepository tripRepository;
    private final TripKeywordRepository tripKeywordRepository;

    public KeywordDevApplicationRunner(final KeywordRepository keywordRepository,
                                       final TripRepository tripRepository,
                                       final TripKeywordRepository tripKeywordRepository) {
        this.keywordRepository = keywordRepository;
        this.tripRepository = tripRepository;
        this.tripKeywordRepository = tripKeywordRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final Resource resource = new ClassPathResource("keyword.json");
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<KeywordRunner> keywordRunners = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<KeywordRunner>>() {});
    }
}
