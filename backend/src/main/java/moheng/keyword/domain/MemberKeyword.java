package moheng.keyword.domain;

import jakarta.persistence.*;
import moheng.global.entity.BaseEntity;
import moheng.member.domain.Member;

@Table(name = "member_keyword")
@Entity
public class MemberKeyword extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;

    protected MemberKeyword() {
    }

    public MemberKeyword(final Member member, final Keyword keyword) {
        this.member = member;
        this.keyword = keyword;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Keyword getKeyword() {
        return keyword;
    }
}
