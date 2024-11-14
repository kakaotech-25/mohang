package moheng.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import moheng.member.domain.GenderType;

import java.time.LocalDate;

public class UpdateProfileRequest {
    @NotBlank(message = "닉네임은 공백일 수 없습니다.")
    private final String nickname;

    @NotBlank(message = "생년월일은 공백일 수 없습니다.")
    private final LocalDate birthday;

    @NotBlank(message = "성별은 공백일 수 없습니다.")
    private final GenderType genderType;

    @NotBlank(message = "프로필 이미지는 공백일 수 없습니다.")
    private final String profileImageUrl;

    public UpdateProfileRequest(final String nickname, final LocalDate birthday,
                                final GenderType genderType, final String profileImageUrl) {
        this.nickname = nickname;
        this.birthday = birthday;
        this.genderType = genderType;
        this.profileImageUrl = profileImageUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public GenderType getGenderType() {
        return genderType;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
