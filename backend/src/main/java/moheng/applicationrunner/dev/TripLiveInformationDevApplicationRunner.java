package moheng.applicationrunner.dev;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import moheng.applicationrunner.dto.LiveInformationRunner;
import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.LiveInformationRepository;
import moheng.liveinformation.domain.TripLiveInformation;
import moheng.liveinformation.domain.TripLiveInformationRepository;
import moheng.liveinformation.exception.NoExistLiveInformationException;
import moheng.trip.domain.Trip;
import moheng.trip.domain.TripRepository;
import moheng.trip.exception.NoExistTripException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(11)
@Component
public class TripLiveInformationDevApplicationRunner implements ApplicationRunner {
    private final TripLiveInformationRepository tripLiveInformationRepository;
    private final TripRepository tripRepository;
    private final LiveInformationRepository liveInformationRepository;

    public TripLiveInformationDevApplicationRunner(final TripLiveInformationRepository tripLiveInformationRepository,
                                                   final TripRepository tripRepository,
                                                   final LiveInformationRepository liveInformationRepository) {
        this.tripLiveInformationRepository = tripLiveInformationRepository;
        this.tripRepository = tripRepository;
        this.liveInformationRepository = liveInformationRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final Resource resource = new ClassPathResource("liveinformation.json");
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<LiveInformationRunner> liveInformationRunners = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<LiveInformationRunner>>() {});

        for(final LiveInformationRunner liveInformationRunner : liveInformationRunners) {
            final Trip trip = tripRepository.findByContentId(liveInformationRunner.getContentid())
                    .orElseThrow(() -> new NoExistTripException("존재하지 않는 여행지입니다."));

            for(final String liveInfoName : liveInformationRunner.getLiveinformation()) {
                LiveInformation liveInformation = liveInformationRepository.findByName(liveInfoName)
                                .orElseThrow(() -> new NoExistLiveInformationException("존재하지 않는 생활정보입니다."));
                tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip));
            }
        }
    }
}
