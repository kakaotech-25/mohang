package moheng.member.domain.repository;

import moheng.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(final String email);
    boolean existsByEmail(final String email);
    boolean existsByNickName(final String nickname);
}
