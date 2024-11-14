package moheng.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CheckDuplicateNicknameRequest {

    @NotBlank(message = "중복 체크할 닉네임은 비어있을 수 없습니다.")
    private String nickname;

    public CheckDuplicateNicknameRequest() {}

    public CheckDuplicateNicknameRequest(final String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
