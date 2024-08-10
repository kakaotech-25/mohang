package moheng.liveinfo.application;

import moheng.liveinfo.domain.LiveInformation;
import moheng.liveinfo.domain.LiveInformationRepository;
import moheng.liveinfo.exception.NoExistLiveInformationException;
import org.springframework.stereotype.Service;

@Service
public class LiveInformationService {
    private final LiveInformationRepository liveInformationRepository;

    public LiveInformationService(LiveInformationRepository liveInformationRepository) {
        this.liveInformationRepository = liveInformationRepository;
    }

    public LiveInformation findByName(String liveTypeName) {
        return liveInformationRepository.findByName(liveTypeName)
                .orElseThrow(() -> new NoExistLiveInformationException("존재하지 않는 생활정보입니다."));
    }
}
