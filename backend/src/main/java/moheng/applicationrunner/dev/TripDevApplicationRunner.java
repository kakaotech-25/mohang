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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Order(2)
@Component
public class TripDevApplicationRunner implements ApplicationRunner {
    private final TripRepository tripRepository;

    public TripDevApplicationRunner(final TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final Resource resource1 = new ClassPathResource("trip.json");
        final Resource resource2 = new ClassPathResource("trip2.json");
        final Resource resource3 = new ClassPathResource("trip3.json");
        final ObjectMapper objectMapper = new ObjectMapper();

        final List<TripRunner> tripRunners = new ArrayList<>();
        tripRunners.addAll(findTripRunnersByResource(resource1, objectMapper));
        tripRunners.addAll(findTripRunnersByResource(resource2, objectMapper));
        tripRunners.addAll(findTripRunnersByResource(resource3, objectMapper));

        for(final TripRunner tripRunner : tripRunners) {
            final Long contentId = tripRunner.getContentid();
            final String title = tripRunner.getTitle();
            final String placeName = tripRunner.getArea_sigungu_combined();
            final String tripImageUrl = tripRunner.getFirstimage();
            final Double mapX = tripRunner.getMapx();
            final Double mapY = tripRunner.getMapy();
            final String description = tripRunner.getOverview();

            tripRepository.save(new Trip(title, placeName, contentId, description, tripImageUrl, mapX, mapY));
        }
    }

    private List<TripRunner> findTripRunnersByResource(final Resource resource, final ObjectMapper objectMapper) throws IOException {
        return objectMapper.readValue(resource.getInputStream(), new TypeReference<List<TripRunner>>() {});
    }
}
