package moheng.applicationrunner.test;


import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.LiveInformationRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(4)
@Component
public class LiveInformationTestApplicationRunner implements ApplicationRunner {

    private final LiveInformationRepository liveInformationRepository;

    public LiveInformationTestApplicationRunner(final LiveInformationRepository liveInformationRepository) {
        this.liveInformationRepository = liveInformationRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        liveInformationRepository.save(new LiveInformation("생활정보1"));
        liveInformationRepository.save(new LiveInformation("생활정보2"));
        liveInformationRepository.save(new LiveInformation("생활정보3"));
        liveInformationRepository.save(new LiveInformation("생활정보4"));
        liveInformationRepository.save(new LiveInformation("생활정보5"));
    }
}