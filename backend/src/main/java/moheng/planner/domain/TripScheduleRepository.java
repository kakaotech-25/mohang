package moheng.planner.domain;

import moheng.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripScheduleRepository extends JpaRepository<TripSchedule, Long> {
    boolean existsByMemberAndName(final Member member, final String name);
}
