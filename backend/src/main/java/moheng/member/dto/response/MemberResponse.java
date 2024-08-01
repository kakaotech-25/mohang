package moheng.member.dto.response;

import moheng.member.domain.GenderType;
import moheng.member.domain.Member;

import java.time.LocalDate;

public class MemberResponse {
    private final Long id;
    private final String profileImageUrl;
    private final String nickname;
    private final LocalDate birthday;
    private final GenderType gender;

    public MemberResponse(final Long id, final String profileImageUrl, final String nickname, final LocalDate birthday, final GenderType gender) {
        this.id = id;
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
    }

    public MemberResponse(final Member member) {
        this.id = member.getId();
        this.profileImageUrl = member.getProfileImageUrl();
        this.nickname = member.getNickName();
        this.birthday = member.getBirthday();
        this.gender = member.getGenderType();
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public GenderType getGender() {
        return gender;
    }
}
