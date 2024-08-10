package moheng.member.application;

import moheng.liveinfo.domain.LiveInformation;
import moheng.liveinfo.domain.LiveInformationRepository;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.dto.request.SignUpLiveInfoRequest;
import moheng.member.dto.request.SignUpProfileRequest;
import moheng.member.dto.request.UpdateProfileRequest;
import moheng.member.dto.response.CheckDuplicateNicknameResponse;
import moheng.member.dto.response.MemberResponse;
import moheng.member.exception.DuplicateNicknameException;
import moheng.member.exception.NoExistMemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final LiveInformationRepository liveInformationRepository;

    public MemberService(MemberRepository memberRepository, LiveInformationRepository liveInformationRepository) {
        this.memberRepository = memberRepository;
        this.liveInformationRepository = liveInformationRepository;
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

        final List<String> liveTypeNames = request.getLiveTypeName();
        for(String liveTypeName : liveTypeNames) {
            LiveInformation liveInformation = liveInformationRepository.findByName(liveTypeName);

        }
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
