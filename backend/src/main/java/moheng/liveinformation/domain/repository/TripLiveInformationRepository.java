package moheng.liveinformation.domain.repository;

import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.TripLiveInformation;
import moheng.trip.domain.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripLiveInformationRepository extends JpaRepository<TripLiveInformation, Long> {
    boolean existsByTripAndLiveInformationIn(final Trip trip, final List<LiveInformation> liveInformations);

    @Query("SELECT tli.liveInformation FROM TripLiveInformation tli WHERE tli.trip IN :trips")
    List<LiveInformation> findLiveInformationByTrips(@Param("trips") final List<Trip> trips);
}
