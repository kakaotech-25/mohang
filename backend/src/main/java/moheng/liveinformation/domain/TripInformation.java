package moheng.liveinformation.domain;

import jakarta.persistence.*;
import moheng.member.domain.Member;
import moheng.trip.domain.Trip;

@Table(name = "trip_information")
@Entity
public class TripInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "live_information_id", nullable = false)
    private LiveInformation liveInformation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    protected TripInformation() {
    }

    public TripInformation(final LiveInformation liveInformation, final Trip trip) {
        this.liveInformation = liveInformation;
        this.trip = trip;
    }
}
