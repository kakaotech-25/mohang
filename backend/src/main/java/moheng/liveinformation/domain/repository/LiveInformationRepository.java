package moheng.liveinformation.domain.repository;

import moheng.liveinformation.domain.LiveInformation;
import moheng.trip.domain.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LiveInformationRepository extends JpaRepository<LiveInformation, Long> {
    Optional<LiveInformation> findByName(final String liveId);

    @Query("SELECT tli.liveInformation FROM TripLiveInformation tli WHERE tli.trip = :trip")
    List<LiveInformation> findLiveInformationByTrip(@Param("trip") final Trip trip);

    boolean existsByName(String name);
}
