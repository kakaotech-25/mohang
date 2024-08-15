package moheng.keyword.domain;

import jakarta.persistence.*;
import moheng.global.entity.BaseEntity;
import moheng.member.domain.Member;
import moheng.trip.domain.Trip;

@Table(name = "trip_keyword")
@Entity
public class TripKeyword extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;

    protected TripKeyword() {
    }

    public TripKeyword(final Trip trip, final Keyword keyword) {
        this.trip = trip;
        this.keyword = keyword;
    }
}
