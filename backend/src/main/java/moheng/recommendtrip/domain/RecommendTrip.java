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

    @Column(name = "visited_count")
    private Long visitedCount;

    protected RecommendTrip() {
    }

    public RecommendTrip(Trip trip, Member member, Long rank) {
        this.trip = trip;
        this.member = member;
        this.rank = rank;
    }

    public RecommendTrip(Trip trip, Member member) {
        this.trip = trip;
        this.member = member;
        this.rank = 1L;
    }

    public void changeRank(Long rank) {
        this.rank = rank;
    }

    public void downRank() {
        this.rank--;
    }

    public Long getRank() {
        return rank;
    }
}
