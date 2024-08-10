package moheng.liveinformation.domain;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberLiveInformationService {
    private final MemberLiveInformationRepository memberLiveInformationRepository;

    public MemberLiveInformationService(MemberLiveInformationRepository memberLiveInformationRepository) {
        this.memberLiveInformationRepository = memberLiveInformationRepository;
    }

    public void saveAll(List<MemberLiveInformation> memberLiveInformations) {
        memberLiveInformationRepository.saveAll(memberLiveInformations);
    }
}
