package moheng.liveinformation.application;

import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.LiveInformationRepository;
import moheng.liveinformation.domain.MemberLiveInformation;
import moheng.liveinformation.domain.MemberLiveInformationRepository;
import moheng.liveinformation.dto.FindMemberLiveInformationResponses;
import moheng.liveinformation.dto.LiveInfoResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberLiveInformationService {
    private final MemberLiveInformationRepository memberLiveInformationRepository;
    private final LiveInformationRepository liveInformationRepository;

    public MemberLiveInformationService(
            final MemberLiveInformationRepository memberLiveInformationRepository,
            final LiveInformationRepository liveInformationRepository) {
        this.memberLiveInformationRepository = memberLiveInformationRepository;
        this.liveInformationRepository = liveInformationRepository;
    }

    public void saveAll(List<MemberLiveInformation> memberLiveInformations) {
        memberLiveInformationRepository.saveAll(memberLiveInformations);
    }

    public FindMemberLiveInformationResponses findMemberSelectedLiveInformation(Long memberId) {
        final List<LiveInformation> allLiveInformation = liveInformationRepository.findAll();
        final List<Long> memberLiveInfoIds = findMemberLiveInfoIds(memberId);
        return new FindMemberLiveInformationResponses(findMemberSelectedLiveInfos(allLiveInformation, memberLiveInfoIds));
    }

    private List<Long> findMemberLiveInfoIds(final Long memberId) {
        return memberLiveInformationRepository.findByMemberId(memberId)
                .stream().map(memberLiveInfo -> memberLiveInfo.getLiveInformation().getId())
                .collect(Collectors.toList());
    }

    private List<LiveInfoResponse> findMemberSelectedLiveInfos(final List<LiveInformation> allLiveInformation, List<Long> selectedLiveInfoIds) {
        return allLiveInformation.stream()
                .map(liveInfo -> new LiveInfoResponse(
                        liveInfo.getId(),
                        liveInfo.getName(),
                        selectedLiveInfoIds.contains(liveInfo.getId())))
                .collect(Collectors.toList());
    }
}
