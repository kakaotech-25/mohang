package moheng.liveinformation.application;

import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.LiveInformationRepository;
import moheng.liveinformation.domain.TripLiveInformation;
import moheng.liveinformation.domain.TripLiveInformationRepository;
import moheng.liveinformation.dto.FindAllLiveInformationResponse;
import moheng.liveinformation.dto.LiveInformationCreateRequest;
import moheng.liveinformation.exception.NoExistLiveInformationException;
import moheng.trip.domain.Trip;
import moheng.trip.domain.TripRepository;
import moheng.trip.exception.NoExistTripException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class LiveInformationService {
    private final LiveInformationRepository liveInformationRepository;
    private final TripLiveInformationRepository tripLiveInformationRepository;
    private final TripRepository tripRepository;

    public LiveInformationService(final LiveInformationRepository liveInformationRepository,
                                  final TripLiveInformationRepository tripLiveInformationRepository, TripRepository tripRepository) {
        this.liveInformationRepository = liveInformationRepository;
        this.tripLiveInformationRepository = tripLiveInformationRepository;
        this.tripRepository = tripRepository;
    }

    public FindAllLiveInformationResponse findAllLiveInformation() {
        return new FindAllLiveInformationResponse(liveInformationRepository.findAll());
    }

    @Transactional
    public void createLiveInformation(LiveInformationCreateRequest request) {
        final LiveInformation liveInformation = new LiveInformation(request.getName());
        liveInformationRepository.save(liveInformation);
    }

    @Transactional
    public void createTripLiveInformation(final long tripId, final long liveInfoId) {
        final Trip trip = tripRepository.findById(tripId)
                        .orElseThrow(() -> new NoExistTripException("존재하지 않는 여행지입니다."));
        final LiveInformation liveInformation = liveInformationRepository.findById(liveInfoId)
                        .orElseThrow(() -> new NoExistLiveInformationException("존재하지 않는 생활정보입니다."));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip));
    }

    @Transactional
    public LiveInformation save(LiveInformation liveInformation) {
        return liveInformationRepository.save(liveInformation);
    }

    public LiveInformation findByName(String liveTypeName) {
        return liveInformationRepository.findByName(liveTypeName)
                .orElseThrow(() -> new NoExistLiveInformationException("존재하지 않는 생활정보입니다."));
    }
}
