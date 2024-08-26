package moheng.planner.domain;

import moheng.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripScheduleRepository extends JpaRepository<TripSchedule, Long> {
    boolean existsByMemberAndName(final Member member, final String name);
    List<TripSchedule> findByMemberOrderByCreatedAtDesc(final Member member);
    List<TripSchedule> findByMemberOrderByStartDate(final Member member);
}
