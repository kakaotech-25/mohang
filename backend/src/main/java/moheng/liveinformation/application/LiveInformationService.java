package moheng.liveinformation.application;

import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.LiveInformationRepository;
import moheng.liveinformation.dto.FindAllLiveInformationResponse;
import moheng.liveinformation.dto.LiveInformationCreateRequest;
import moheng.liveinformation.exception.NoExistLiveInformationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LiveInformationService {
    private final LiveInformationRepository liveInformationRepository;

    public LiveInformationService(LiveInformationRepository liveInformationRepository) {
        this.liveInformationRepository = liveInformationRepository;
    }

    public FindAllLiveInformationResponse findAllLiveInformation() {
        return new FindAllLiveInformationResponse(liveInformationRepository.findAll());
    }

    public void createLiveInformation(LiveInformationCreateRequest request) {
        final LiveInformation liveInformation = new LiveInformation(request.getName());
        liveInformationRepository.save(liveInformation);
    }

    public LiveInformation save(LiveInformation liveInformation) {
        return liveInformationRepository.save(liveInformation);
    }

    public LiveInformation findByName(String liveTypeName) {
        return liveInformationRepository.findByName(liveTypeName)
                .orElseThrow(() -> new NoExistLiveInformationException("존재하지 않는 생활정보입니다."));
    }

    public
}
