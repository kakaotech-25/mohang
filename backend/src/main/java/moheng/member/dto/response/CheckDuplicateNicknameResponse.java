package moheng.member.dto.response;

public class CheckDuplicateNicknameResponse {
    private final String message;

    public CheckDuplicateNicknameResponse() {
        this.message = "사용 가능한 닉네임입니다.";
    }

    public String getMessage() {
        return message;
    }
}
