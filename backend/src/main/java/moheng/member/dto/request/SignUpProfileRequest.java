package moheng.member.dto.request;

import moheng.member.domain.GenderType;

import java.time.LocalDate;

public class SignUpProfileRequest {
    private final String nickname;
    private final LocalDate birthday;
    private final GenderType genderType;

    public SignUpProfileRequest(final String nickname, final LocalDate birthday,
                                final GenderType genderType) {
        this.nickname = nickname;
        this.birthday = birthday;
        this.genderType = genderType;}

    public String getNickname() {
        return nickname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public GenderType getGenderType() {
        return genderType;
    }
}
