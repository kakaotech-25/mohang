package moheng.recommendtrip.domain;

import jakarta.persistence.*;
import moheng.global.entity.BaseEntity;
import moheng.member.domain.Member;
import moheng.trip.domain.Trip;

@Table(name = "recommend_trip")
@Entity
public class RecommendTrip extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "rank")
    private Long rank;

    protected RecommendTrip() {
    }

    public RecommendTrip(final Trip trip, final Member member, final Long rank) {
        this.trip = trip;
        this.member = member;
        this.rank = rank;
    }

    public RecommendTrip(final Trip trip, final Member member) {
        this.trip = trip;
        this.member = member;
        this.rank = 1L;
    }

    public Long getId() {
        return id;
    }

    public Long getRank() {
        return rank;
    }

    public Trip getTrip() {
        return trip;
    }
}
