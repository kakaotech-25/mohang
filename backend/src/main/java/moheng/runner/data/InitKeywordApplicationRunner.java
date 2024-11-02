package moheng.runner.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import moheng.runner.data.dto.KeywordRunner;
import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.repository.KeywordRepository;
import moheng.keyword.domain.TripKeyword;
import moheng.keyword.domain.repository.TripKeywordRepository;
import moheng.trip.domain.Trip;
import moheng.trip.domain.repository.TripRepository;
import moheng.trip.exception.NoExistTripException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile({"local", "dev", "prod"})
@Order(3)
@Component
public class InitKeywordApplicationRunner implements ApplicationRunner {
    private final KeywordRepository keywordRepository;
    private final TripRepository tripRepository;
    private final TripKeywordRepository tripKeywordRepository;

    public InitKeywordApplicationRunner(final KeywordRepository keywordRepository,
                                        final TripRepository tripRepository,
                                        final TripKeywordRepository tripKeywordRepository) {
        this.keywordRepository = keywordRepository;
        this.tripRepository = tripRepository;
        this.tripKeywordRepository = tripKeywordRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(keywordRepository.count() == 0) {
            final Resource resource = new ClassPathResource("json/keyword.json");
            final ObjectMapper objectMapper = new ObjectMapper();
            final List<KeywordRunner> keywordRunners = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<KeywordRunner>>() {});

            for(final KeywordRunner keywordRunner : keywordRunners) {
                Trip trip = tripRepository.findByContentId(keywordRunner.getContentid())
                        .orElseThrow(NoExistTripException::new);

                for(final String keywordName : keywordRunner.getFiltered_labels()) {
                    final Keyword keyword = findOrCreateKeyword(keywordName);
                    tripKeywordRepository.save(new TripKeyword(trip, keyword));
                }
            }
        }
    }

    private Keyword findOrCreateKeyword(final String name) {
        if(keywordRepository.existsByName(name)) {
            return keywordRepository.findByName(name);
        }
        return keywordRepository.save(new Keyword(name));
    }
}
