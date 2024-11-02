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

    @Column(name = "ranking")
    private Long ranking;

    protected RecommendTrip() {
    }

    public RecommendTrip(final Trip trip, final Member member, final Long ranking) {
        this.trip = trip;
        this.member = member;
        this.ranking = ranking;
    }

    public RecommendTrip(final Trip trip, final Member member) {
        this.trip = trip;
        this.member = member;
        this.ranking = 1L;
    }

    public Long getId() {
        return id;
    }

    public Long getRanking() {
        return ranking;
    }

    public Trip getTrip() {
        return trip;
    }
}
