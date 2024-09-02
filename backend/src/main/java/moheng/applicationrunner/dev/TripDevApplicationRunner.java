package moheng.applicationrunner.dev;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import moheng.applicationrunner.dto.LiveInformationRunner;
import moheng.applicationrunner.dto.TripRunner;
import moheng.trip.domain.Trip;
import moheng.trip.domain.TripRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(10)
@Component
public class TripDevApplicationRunner implements ApplicationRunner {
    private final TripRepository tripRepository;

    public TripDevApplicationRunner(final TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final Resource resource = new ClassPathResource("triptmp.json");
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<TripRunner> tripRunners = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<TripRunner>>() {});

        for(final TripRunner tripRunner : tripRunners) {
            final Long contentId = tripRunner.getContentid();
            final String title = tripRunner.getTitle();
            final String tripImageUrl = tripRunner.getFirstimage();
            final Double mapX = tripRunner.getMapx();
            final Double mapY = tripRunner.getMapy();
            final String description = tripRunner.getOverview();

            if(!description.isEmpty()) {
                tripRepository.save(new Trip(title, title, contentId, description, tripImageUrl, mapX, mapY));
            }
        }
    }
}
