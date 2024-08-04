package moheng.member.application;

import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.dto.request.SignUpProfileRequest;
import moheng.member.dto.response.MemberResponse;
import moheng.member.exception.NoExistMemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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
        final Member updateProfileMember = new Member(
                memberId,
                request.getNickname(),
                request.getBirthday(),
                request.getGenderType()
        );
        memberRepository.save(updateProfileMember);
    }
}
