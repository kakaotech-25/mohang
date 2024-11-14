package moheng.keyword.dto.request;

import jakarta.validation.constraints.NotBlank;

public class KeywordCreateRequest {

    @NotBlank(message = "공백일 수 없습니다.")
    private String keyword;

    private KeywordCreateRequest() {
    }

    public KeywordCreateRequest(final String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}
