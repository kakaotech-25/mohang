package moheng.member.dto.request;

public class CheckDuplicateNicknameRequest {
    private String nickname;

    public CheckDuplicateNicknameRequest() {}

    public CheckDuplicateNicknameRequest(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
