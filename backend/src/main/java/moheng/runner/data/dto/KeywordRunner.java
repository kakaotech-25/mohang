package moheng.runner.data.dto;

import java.util.List;

public class KeywordRunner {
    private Long contentid;
    private List<String> filtered_labels;

    private KeywordRunner() {
    }

    public KeywordRunner(final Long contentid, final List<String> filtered_labels) {
        this.contentid = contentid;
        this.filtered_labels = filtered_labels;
    }

    public Long getContentid() {
        return contentid;
    }

    public List<String> getFiltered_labels() {
        return filtered_labels;
    }
}
