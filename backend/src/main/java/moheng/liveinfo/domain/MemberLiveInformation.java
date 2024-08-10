package moheng.liveinfo.domain;

import jakarta.persistence.*;
import moheng.member.domain.Member;

@Table(name = "member_live_information")
@Entity
public class MemberLiveInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "live_information_id", nullable = false)
    private LiveInformation liveInformation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    protected MemberLiveInformation() {
    }

    public MemberLiveInformation(LiveInformation liveInformation, Member member) {
        this.liveInformation = liveInformation;
        this.member = member;
    }
}
