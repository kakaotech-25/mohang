package moheng.trip.domain.repository;

import jakarta.persistence.LockModeType;
import moheng.trip.domain.Trip;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Trip> findByIdForUpdate(final Long id);

    Optional<Trip> findByContentId(final Long contentId);

    List<Trip> findTop30ByOrderByVisitedCountDesc();

    @Query("SELECT k.name FROM Keyword k WHERE k.id IN :keywordIds")
    List<String> findNamesByIds(@Param("keywordIds") final List<Long> keywordIds);

    @Query("SELECT t FROM Trip t WHERE t.contentId IN :contentIds")
    List<Trip> findTripsByContentIds(final List<Long> contentIds);

    @Query("SELECT t FROM Trip t " +
            "JOIN TripLiveInformation tl ON t.id = tl.trip.id " +
            "JOIN LiveInformation li ON tl.liveInformation.id = li.id " +
            "WHERE li.id = :liveInformationId " +
            "AND t.contentId IN :contentIds")
    List<Trip> findFilteredTripsByLiveInformation(@Param("liveInformationId") final Long liveInformationId,
                                                  @Param("contentIds") final List<Long> contentIds);

    @Query("SELECT t FROM Trip t JOIN TripScheduleRegistry tsr ON t.id = tsr.trip.id WHERE tsr.tripSchedule.id = :scheduleId")
    List<Trip> findTripsByScheduleId(@Param("scheduleId") final Long scheduleId);

}
