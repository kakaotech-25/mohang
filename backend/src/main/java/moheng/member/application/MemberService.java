package moheng.member.application;

import moheng.auth.domain.oauth.Authority;
import moheng.liveinformation.application.LiveInformationService;
import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.MemberLiveInformation;
import moheng.liveinformation.application.MemberLiveInformationService;
import moheng.liveinformation.exception.EmptyLiveInformationException;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.dto.request.SignUpInterestTripsRequest;
import moheng.member.dto.request.SignUpLiveInfoRequest;
import moheng.member.dto.request.SignUpProfileRequest;
import moheng.member.dto.request.UpdateProfileRequest;
import moheng.member.dto.response.MemberResponse;
import moheng.member.exception.DuplicateNicknameException;
import moheng.member.exception.NoExistMemberException;
import moheng.member.exception.ShortContentidsSizeException;
import moheng.recommendtrip.application.RecommendTripService;
import moheng.trip.application.TripService;
import moheng.trip.domain.Trip;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class MemberService {
    private static final long MAX_RECOMMEND_TRIP_SIZE = 10L;
    private static final long MIN_RECOMMEND_TRIP_SIZE = 5L;

    private final MemberRepository memberRepository;
    private final LiveInformationService liveInformationService;
    private final MemberLiveInformationService memberLiveInformationService;
    private final TripService tripService;
    private final RecommendTripService recommendTripService;

    public MemberService(final MemberRepository memberRepository,
                         final LiveInformationService liveInformationService,
                         final MemberLiveInformationService memberLiveInformationService,
                         final TripService tripService,
                         final RecommendTripService recommendTripService) {
        this.memberRepository = memberRepository;
        this.liveInformationService = liveInformationService;
        this.memberLiveInformationService = memberLiveInformationService;
        this.tripService = tripService;
        this.recommendTripService = recommendTripService;
    }

    public MemberResponse findById(final Long id) {
        Member foundMember = memberRepository.findById(id)
                .orElseThrow(NoExistMemberException::new);
        return new MemberResponse(foundMember);
    }

    public Member findByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(NoExistMemberException::new);
    }

    public boolean existsByEmail(final String email) {
        return memberRepository.existsByEmail(email);
    }

    @Transactional
    public void save(Member member) {
        memberRepository.save(member);
    }

    public boolean existsByNickname(final String nickname) {
        return memberRepository.existsByNickName(nickname);
    }

    @Transactional
    public void signUpByProfile(final long memberId, final SignUpProfileRequest request) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoExistMemberException("존재하지 않는 회원입니다."));

        checkIsAlreadyExistNickname(request.getNickname());

        final Member updateProfileMember = new Member(
                member.getId(),
                request.getNickname(),
                request.getBirthday(),
                request.getGenderType()
        );
        memberRepository.save(updateProfileMember);
    }

    @Transactional
    public void signUpByLiveInfo(final long memberId, final SignUpLiveInfoRequest request) {
        final Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new NoExistMemberException("존재하지 않는 회원입니다."));
        saveMemberLiveInformation(request.getLiveInfoNames(), member);
    }

    private void saveMemberLiveInformation(List<String> liveTypeNames, Member member) {
        validateLiveTypeNames(liveTypeNames);
        final List<MemberLiveInformation> memberLiveInformationList = new ArrayList<>();

        for(String liveTypeName : liveTypeNames) {
            final LiveInformation liveInformation = liveInformationService.findByName(liveTypeName);
            memberLiveInformationList.add(new MemberLiveInformation(liveInformation, member));
        }
        memberLiveInformationService.saveAll(memberLiveInformationList);
    }

    private void validateLiveTypeNames(List<String> liveTypeNames) {
        if(liveTypeNames == null || liveTypeNames.isEmpty()) {
            throw new EmptyLiveInformationException("생활정보를 선택하지 않았습니다.");
        }
    }

    @Transactional
    public void signUpByInterestTrips(long memberId, SignUpInterestTripsRequest request) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoExistMemberException("존재하지 않는 회원입니다."));

        saveRecommendTrip(request.getContentIds(), member, 1L);
        changeMemberPrivileges(member);
    }

    private void saveRecommendTrip(List<Long> contentIds, Member member, long rank) {
        validateContentIds(contentIds);
        for (final Long contentId : contentIds) {
            Trip trip = tripService.findByContentId(contentId);
            recommendTripService.saveByRank(trip, member, rank++);
        }
    }

    private void validateContentIds(List<Long> contentIds) {
        if(contentIds.size() < MIN_RECOMMEND_TRIP_SIZE || contentIds.size() > MAX_RECOMMEND_TRIP_SIZE)  {
            throw new ShortContentidsSizeException("AI 맞춤 추천을 위해 관심 여행지를 5개 이상, 10개 이하로 선택해야합니다.");
        }
    }

    private void changeMemberPrivileges(Member member) {
        member.changePrivilege(Authority.REGULAR_MEMBER);
    }

    @Transactional
    public void updateByProfile(final long memberId, final UpdateProfileRequest request) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoExistMemberException("존재하지 않는 회원입니다."));

        if(member.isNicknameChanged(request.getNickname())) {
            checkIsAlreadyExistNickname(request.getNickname());
        }
        final Member updateMember = new Member(
                memberId,
                request.getNickname(),
                request.getBirthday(),
                request.getGenderType(),
                request.getProfileImageUrl()
        );
        memberRepository.save(updateMember);
    }

    public void checkIsAlreadyExistNickname(String nickname) {
        if(isAlreadyExistNickname(nickname)) {
            throw new DuplicateNicknameException("중복되는 닉네임이 존재합니다.");
        }
    }

    private boolean isAlreadyExistNickname(final String nickname) {
        return memberRepository.existsByNickName(nickname);
    }
}
