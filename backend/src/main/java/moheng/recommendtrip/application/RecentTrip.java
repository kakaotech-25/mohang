package moheng.recommendtrip.application;

import jakarta.persistence.*;
import moheng.global.entity.BaseEntity;
import moheng.member.domain.Member;
import moheng.trip.domain.Trip;

@Table(name = "recent_trip")
@Entity
public class RecentTrip extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommand_trip_id", nullable = false)
    private RecommendTrip recommendTrip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "rank")
    private Long rank;

    @Column(name = "visited_count")
    private Long visitedCount;

    protected RecentTrip() {
    }

    public RecentTrip(Member member, RecommendTrip recommendTrip) {
        this.member = member;
        this.recommendTrip = recommendTrip;
    }
}
