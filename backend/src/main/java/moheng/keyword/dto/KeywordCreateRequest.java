package moheng.keyword.dto;

public class KeywordCreateRequest {
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
