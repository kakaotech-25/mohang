package moheng.member.dto.request;

import java.util.List;

public class SignUpLiveInfoRequest {
    private List<Long> liveInfoIds;

    private SignUpLiveInfoRequest() {
    }

    public SignUpLiveInfoRequest(final List<Long> liveInfoIds) {
        this.liveInfoIds = liveInfoIds;
    }

    public List<Long> getLiveInfoIds() {
        return liveInfoIds;
    }
}
