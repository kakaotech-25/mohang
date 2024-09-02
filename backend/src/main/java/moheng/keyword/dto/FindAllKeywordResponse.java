package moheng.keyword.dto;

import moheng.keyword.domain.Keyword;

public class FindAllKeywordResponse {
    private Long keywordId;
    private String name;

    private FindAllKeywordResponse() {
    }

    public FindAllKeywordResponse(final Keyword keyword) {
        this.keywordId = keyword.getId();
        this.name = keyword.getName();
    }

    public Long getKeywordId() {
        return keywordId;
    }

    public String getName() {
        return name;
    }
}
