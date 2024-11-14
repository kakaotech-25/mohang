package moheng.member.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import moheng.member.domain.GenderType;

import java.time.LocalDate;

public class SignUpProfileRequest {

    @NotBlank(message = "닉네임은 공백일 수 없습니다.")
    private final String nickname;

    @NotBlank(message = "생년월일은 공백일 수 없습니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate birthday;

    @NotBlank(message = "성별은 공백일 수 없습니다.")
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
