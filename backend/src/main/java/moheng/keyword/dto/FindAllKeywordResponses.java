package moheng.keyword.dto;

import moheng.keyword.domain.Keyword;

import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

public class FindAllKeywordResponses {
    private List<FindAllKeywordResponse> findAllKeywordResponses;

    private FindAllKeywordResponses() {
    }

    public FindAllKeywordResponses(final List<Keyword> keywords) {
        findAllKeywordResponses = toResponses(keywords);
    }

    private List<FindAllKeywordResponse> toResponses(final List<Keyword> keywords) {
        return keywords.stream()
                .map(FindAllKeywordResponse::new)
                .collect(Collectors.toList());
    }

    public List<FindAllKeywordResponse> getFindAllKeywordResponses() {
        return findAllKeywordResponses;
    }
}
