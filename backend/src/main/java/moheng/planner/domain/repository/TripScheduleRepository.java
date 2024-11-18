package moheng.planner.domain.repository;

import moheng.member.domain.Member;
import moheng.planner.domain.TripSchedule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TripScheduleRepository extends JpaRepository<TripSchedule, Long> {
    boolean existsByMemberAndName(final Member member, final String name);
    List<TripSchedule> findByMemberOrderByCreatedAtDesc(final Member member);
    List<TripSchedule> findByMemberOrderByStartDateAsc(final Member member);
    List<TripSchedule> findByMemberOrderByNameAsc(final Member member);

    @Query("select t from TripSchedule t " +
            "where t.isPrivate = false " +
            "and t.startDate >= :startOfMonth and t.endDate <= :endOfMonth " +
            "order by t.createdAt DESC")
    List<TripSchedule> findPublicSchedulesForCurrentMonth(final LocalDate startOfMonth, final LocalDate endOfMonth);

    @Query("select t from TripSchedule t where t.member = :member " +
            "and t.createdAt >= :startDate and t.createdAt <= :endDate " +
            "order by t.createdAt DESC")
    List<TripSchedule> findByMemberAndDateRangeOrderByCreatedAt(final Member member, final LocalDateTime startDate, final LocalDateTime endDate);

    @Query("select t from TripSchedule t where t.isPrivate = false " +
            "and t.createdAt >= :startDate and t.createdAt <= :endDate " +
            "order by t.createdAt DESC")
    List<TripSchedule> findPublicSchedulesForCreatedAtRange(final LocalDateTime startDate, final LocalDateTime endDate, final Pageable pageable);

    List<TripSchedule> findByNameContainsAndPrivateIsFalse(final String name);
}
