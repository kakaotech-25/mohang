package moheng.recommendtrip.application;

import jakarta.persistence.*;
import moheng.global.entity.BaseEntity;
import moheng.member.domain.Member;

@Table(name = "recommend_trip")
@Entity
public class RecommendTrip extends BaseEntity {
    private static final long MAX_SIZE = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    protected RecommendTrip() {
    }
}
