package moheng.keyword.domain;

import moheng.trip.domain.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripKeywordRepository extends JpaRepository<TripKeyword, Long> {
    @Query("SELECT tk.trip FROM TripKeyword tk WHERE tk.keyword.id IN :keywordIds")
    List<Trip> findTripsByKeywordIds(@Param("keywordIds") final List<Long> keywordIds);
}
