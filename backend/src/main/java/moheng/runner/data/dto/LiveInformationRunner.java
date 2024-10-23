package moheng.runner.data.dto;

import java.util.List;

public class LiveInformationRunner {
    private Long contentid;
    private List<String> liveinformation;

    private LiveInformationRunner() {
    }

    public LiveInformationRunner(final Long contentid, final List<String> liveinformation) {
        this.contentid = contentid;
        this.liveinformation = liveinformation;
    }

    public List<String> getLiveinformation() {
        return liveinformation;
    }

    public Long getContentid() {
        return contentid;
    }
}
