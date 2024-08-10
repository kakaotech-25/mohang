package moheng.member.dto.request;

import java.util.List;

public class SignUpLiveInfoRequest {
    private final List<String> liveInfoNames;

    public SignUpLiveInfoRequest(List<String> liveInfoNames) {
        this.liveInfoNames = liveInfoNames;
    }

    public List<String> getLiveTypeName() {
        return liveInfoNames;
    }
}
