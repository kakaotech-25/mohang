package moheng.planner.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TripScheduleRegistryRepository extends JpaRepository<TripScheduleRegistry, Long> {
    @Modifying
    @Query("DELETE FROM TripScheduleRegistry tsr WHERE tsr.tripSchedule.id = :tripScheduleId")
    void deleteAllByTripScheduleId(@Param("tripScheduleId") final Long tripScheduleId);
}
