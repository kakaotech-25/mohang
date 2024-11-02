package moheng.trip.domain;

import jakarta.persistence.*;
import moheng.global.entity.BaseEntity;
import moheng.member.domain.Member;

@Table(name = "member_trip")
@Entity
public class MemberTrip extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Trip trip;

    @Column(name = "visited_count")
    private Long visitedCount;

    protected MemberTrip() {
    }

    public MemberTrip(final Member member, final Trip trip) {
        this.member = member;
        this.trip = trip;
        visitedCount = 0L;
    }

    public MemberTrip(final Member member, final Trip trip, final Long visitedCount) {
        this.member = member;
        this.trip = trip;
        this.visitedCount = visitedCount;
    }

    public void incrementVisitedCount() {
        visitedCount++;
    }

    public Long getVisitedCount() {
        return visitedCount;
    }

    public Trip getTrip() {
        return trip;
    }
}
