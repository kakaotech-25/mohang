package moheng.applicationrunner.dev;

import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.LiveInformationRepository;
import moheng.liveinformation.domain.TripLiveInformation;
import moheng.liveinformation.domain.TripLiveInformationRepository;
import moheng.trip.domain.Trip;
import moheng.trip.domain.TripRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(6)
@Component
public class TripLiveInformationApplicationRunner implements ApplicationRunner {
    private final TripLiveInformationRepository tripLiveInformationRepository;
    private final TripRepository tripRepository;
    private final LiveInformationRepository liveInformationRepository;

    public TripLiveInformationApplicationRunner(final TripLiveInformationRepository tripLiveInformationRepository,
                                                final TripRepository tripRepository,
                                                final LiveInformationRepository liveInformationRepository) {
        this.tripLiveInformationRepository = tripLiveInformationRepository;
        this.tripRepository = tripRepository;
        this.liveInformationRepository = liveInformationRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        Trip trip1 = tripRepository.findById(1L).get();
        Trip trip2 = tripRepository.findById(2L).get();
        Trip trip3 = tripRepository.findById(3L).get();
        Trip trip4 = tripRepository.findById(4L).get();
        Trip trip5 = tripRepository.findById(5L).get();
        Trip trip6 = tripRepository.findById(6L).get();
        Trip trip7 = tripRepository.findById(7L).get();
        Trip trip8 = tripRepository.findById(8L).get();
        Trip trip9 = tripRepository.findById(9L).get();
        Trip trip10 = tripRepository.findById(10L).get();

        LiveInformation liveInformation1 = liveInformationRepository.findById(1L).get();
        LiveInformation liveInformation2 = liveInformationRepository.findById(2L).get();
        LiveInformation liveInformation3 = liveInformationRepository.findById(3L).get();
        LiveInformation liveInformation4 = liveInformationRepository.findById(4L).get();
        LiveInformation liveInformation5 = liveInformationRepository.findById(5L).get();

        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation1, trip1));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation1, trip2));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation2, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation2, trip4));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation3, trip5));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation3, trip6));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation4, trip7));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation4, trip8));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation5, trip9));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation5, trip10));
    }
}
