package moheng.member.dto.request;

import java.util.List;

public class SignUpLiveInfoRequest {
    private final List<String> liveTypeNames;

    public SignUpLiveInfoRequest(List<String> liveTypeNames) {
        this.liveTypeNames = liveTypeNames;
    }

    public List<String> getLiveTypeName() {
        return liveTypeNames;
    }
}
