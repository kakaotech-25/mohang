package moheng.liveinfo.domain;

import org.springframework.stereotype.Service;

@Service
public class MemberLiveInformationService {
    private final MemberLiveInformationRepository memberLiveInformationRepository;

    public MemberLiveInformationService(MemberLiveInformationRepository memberLiveInformationRepository) {
        this.memberLiveInformationRepository = memberLiveInformationRepository;
    }

    public void save(MemberLiveInformation memberLiveInformation) {
        memberLiveInformationRepository.save(memberLiveInformation);
    }
}
