package moheng.trip.repository;

import moheng.trip.domain.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<Trip> findByContentId(Long contentId);
    List<Trip> findTop30ByOrderByVisitedCountDesc();
}
