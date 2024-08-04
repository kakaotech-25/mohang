package moheng.member.dto.request;

public class CheckDuplicateNicknameRequest {
    private final String nickname;

    public CheckDuplicateNicknameRequest(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
