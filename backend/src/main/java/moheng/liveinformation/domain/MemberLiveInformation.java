package moheng.liveinformation.domain;

import jakarta.persistence.*;
import moheng.global.annotation.Generated;
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

    public MemberLiveInformation(final LiveInformation liveInformation, final Member member) {
        this.liveInformation = liveInformation;
        this.member = member;
    }

    @Generated
    public Long getId() {
        return id;
    }

    public LiveInformation getLiveInformation() {
        return liveInformation;
    }

    @Generated
    public Member getMember() {
        return member;
    }
}
