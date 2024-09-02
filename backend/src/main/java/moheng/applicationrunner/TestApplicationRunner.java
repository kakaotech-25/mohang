package moheng.applicationrunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import moheng.applicationrunner.dto.LiveInformationRunner;
import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.LiveInformationRepository;
import moheng.liveinformation.domain.TripLiveInformationRepository;
import moheng.trip.domain.TripRepository;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.print.DocFlavor;
import java.io.FileReader;
import java.util.List;

@Order(9)
@Component
public class TestApplicationRunner implements ApplicationRunner {
    private final LiveInformationRepository liveInformationRepository;
    private final TripRepository tripRepository;
    private final TripLiveInformationRepository tripLiveInformationRepository;

    public TestApplicationRunner(final LiveInformationRepository liveInformationRepository,
                                 final TripRepository tripRepository,
                                 final TripLiveInformationRepository tripLiveInformationRepository) {
        this.liveInformationRepository = liveInformationRepository;
        this.tripRepository = tripRepository;
        this.tripLiveInformationRepository = tripLiveInformationRepository;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        final Resource resource = new ClassPathResource("liveinformation.json");
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<LiveInformationRunner> liveInformationRunners = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<LiveInformationRunner>>() {});

        for(final LiveInformationRunner liveInformationRunner : liveInformationRunners) {
            for(final String liveInfoName : liveInformationRunner.getLiveinformation()) {
                liveInformationRepository.save(new LiveInformation(liveInfoName));
            }
        }
    }
}
