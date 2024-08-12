package moheng.member.dto.request;

import java.util.List;

public class SignUpLiveInfoRequest {
    private List<String> liveInfoNames;

    private SignUpLiveInfoRequest() {
    }

    public SignUpLiveInfoRequest(List<String> liveInfoNames) {
        this.liveInfoNames = liveInfoNames;
    }

    public List<String> getLiveInfoNames() {
        return liveInfoNames;
    }
}
