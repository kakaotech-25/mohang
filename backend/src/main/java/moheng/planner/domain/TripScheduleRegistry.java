package moheng.planner.domain;

import jakarta.persistence.*;
import moheng.trip.domain.Trip;

@Table(name = "trip_schedule_registry")
@Entity
public class TripScheduleRegistry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_schedule", nullable = false)
    private TripSchedule tripSchedule;

    protected TripScheduleRegistry() {
    }

    public TripScheduleRegistry(final Trip trip, final TripSchedule tripSchedule) {
        this.trip = trip;
        this.tripSchedule = tripSchedule;
    }
}
