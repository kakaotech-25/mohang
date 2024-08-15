package moheng.keyword.domain;

import moheng.trip.domain.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripKeywordRepository extends JpaRepository<TripKeyword, Long> {
    @Query("SELECT tk FROM TripKeyword tk WHERE tk.keyword.id IN :keywordIds")
    List<TripKeyword> findTripKeywordsByKeywordIds(@Param("keywordIds") final List<Long> keywordIds);

    @Query("SELECT tk FROM TripKeyword tk WHERE tk.trip IN :trips")
    List<TripKeyword> findByTrips(@Param("trips") final List<Trip> trips);

    @Query("SELECT tk FROM TripKeyword tk WHERE tk.trip = :trip")
    List<TripKeyword> findByTrip(@Param("trip") final Trip trip);
}
