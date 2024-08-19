package moheng.trip.domain;

import moheng.member.domain.Member;
import moheng.recommendtrip.domain.RecommendTrip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberTripRepository extends JpaRepository<MemberTrip, Long> {
    boolean existsByMemberAndTrip(final Member member, final Trip trip);
    MemberTrip findByMemberAndTrip(final Member member, final Trip trip);
    List<MemberTrip> findByMember(final Member member);
}
