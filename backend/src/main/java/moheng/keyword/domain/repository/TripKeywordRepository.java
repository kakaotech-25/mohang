package moheng.keyword.domain.repository;

import moheng.keyword.domain.TripKeyword;
import moheng.trip.domain.Trip;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripKeywordRepository extends JpaRepository<TripKeyword, Long> {
    @EntityGraph(attributePaths = {"trip"})
    @Query("SELECT tk FROM TripKeyword tk WHERE tk.keyword.id IN :keywordIds")
    List<TripKeyword> findTripKeywordsByKeywordIds(@Param("keywordIds") final List<Long> keywordIds);

    @Query(value = "SELECT * FROM trip_keyword tk WHERE tk.keyword_id = :keywordId LIMIT 30", nativeQuery = true)
    List<TripKeyword> findTop30ByKeywordId(@Param("keywordId") final Long keywordId);

    @Query("SELECT tk FROM TripKeyword tk WHERE tk.trip IN :trips")
    List<TripKeyword> findByTrips(@Param("trips") final List<Trip> trips);

    @Query("SELECT tk FROM TripKeyword tk WHERE tk.trip = :trip")
    List<TripKeyword> findByTrip(@Param("trip") final Trip trip);
}
