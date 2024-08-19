package moheng.liveinformation.domain;

import moheng.trip.domain.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripLiveInformationRepository extends JpaRepository<TripLiveInformation, Long> {
    boolean existsByTripAndLiveInformationIn(final Trip trip, final List<LiveInformation> liveInformations);
}
