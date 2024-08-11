package moheng.liveinformation.application;

import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.LiveInformationRepository;
import moheng.liveinformation.domain.MemberLiveInformation;
import moheng.liveinformation.domain.MemberLiveInformationRepository;
import moheng.liveinformation.dto.FindMemberLiveInformationResponses;
import moheng.liveinformation.dto.LiveInfoResponse;
import moheng.liveinformation.dto.UpdateMemberLiveInformationRequest;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class MemberLiveInformationService {
    private final MemberLiveInformationRepository memberLiveInformationRepository;
    private final LiveInformationRepository liveInformationRepository;
    private final MemberRepository memberRepository;

    public MemberLiveInformationService(
            final MemberLiveInformationRepository memberLiveInformationRepository,
            final LiveInformationRepository liveInformationRepository,
            final MemberRepository memberRepository) {
        this.memberLiveInformationRepository = memberLiveInformationRepository;
        this.liveInformationRepository = liveInformationRepository;
        this.memberRepository = memberRepository;
    }

    public void saveAll(List<MemberLiveInformation> memberLiveInformations) {
        memberLiveInformationRepository.saveAll(memberLiveInformations);
    }

    @Transactional
    public void updateMemberLiveInformation(long memberId, UpdateMemberLiveInformationRequest request) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoExistMemberException("존재하지 않는 회원입니다."));

        List<LiveInformation> liveInformations = liveInformationRepository.findAllById(request.getContentIds());
        memberLiveInformationRepository.deleteByMemberId(memberId);
       saveMemberLiveInformation(liveInformations, member);
    }

    private void saveMemberLiveInformation(List<LiveInformation> liveInformations, Member member) {
        List<MemberLiveInformation> updateMemberLiveInfoList = new ArrayList<>();

        for (LiveInformation liveInformation : liveInformations) {
            final MemberLiveInformation memberLiveInformation = new MemberLiveInformation(liveInformation, member);
            updateMemberLiveInfoList.add(memberLiveInformation);
        }
        memberLiveInformationRepository.saveAll(updateMemberLiveInfoList);
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
