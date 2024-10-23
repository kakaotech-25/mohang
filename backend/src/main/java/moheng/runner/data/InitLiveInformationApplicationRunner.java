package moheng.runner.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import moheng.runner.data.dto.LiveInformationRunner;
import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.repository.LiveInformationRepository;
import moheng.liveinformation.domain.TripLiveInformation;
import moheng.liveinformation.domain.repository.TripLiveInformationRepository;
import moheng.liveinformation.exception.NoExistLiveInformationException;
import moheng.trip.domain.Trip;
import moheng.trip.domain.repository.TripRepository;
import moheng.trip.exception.NoExistTripException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile({"local", "dev", "prod"})
@Order(2)
@Component
public class InitLiveInformationApplicationRunner implements ApplicationRunner {
    private final TripRepository tripRepository;
    private final LiveInformationRepository liveInformationRepository;
    private final TripLiveInformationRepository tripLiveInformationRepository;

    public InitLiveInformationApplicationRunner(final TripRepository tripRepository,
                                                final LiveInformationRepository liveInformationRepository,
                                                final TripLiveInformationRepository tripLiveInformationRepository) {
        this.tripRepository = tripRepository;
        this.liveInformationRepository = liveInformationRepository;
        this.tripLiveInformationRepository = tripLiveInformationRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(liveInformationRepository.count() == 0) {
            final Resource resource = new ClassPathResource("json/liveinformation.json");
            final ObjectMapper objectMapper = new ObjectMapper();
            final List<LiveInformationRunner> liveInformationRunners = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<LiveInformationRunner>>() {});

            for(final LiveInformationRunner liveInformationRunner : liveInformationRunners) {
                final Trip trip = tripRepository.findByContentId(liveInformationRunner.getContentid())
                                .orElseThrow(NoExistTripException::new);

                for(final String liveInfoName : liveInformationRunner.getLiveinformation()) {
                    final LiveInformation liveInformation = findOrCreateLiveInformation(liveInfoName);
                    tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip));
                }
            }
        }
    }

    private LiveInformation findOrCreateLiveInformation(final String liveInfoName) {
        if(liveInformationRepository.existsByName(liveInfoName)) {
            return liveInformationRepository.findByName(liveInfoName)
                    .orElseThrow(NoExistLiveInformationException::new);
        }
        return liveInformationRepository.save(new LiveInformation(liveInfoName));
    }
}
