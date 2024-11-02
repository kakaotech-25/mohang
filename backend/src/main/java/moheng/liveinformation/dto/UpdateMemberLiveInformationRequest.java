package moheng.liveinformation.dto;

import java.util.List;

public class UpdateMemberLiveInformationRequest {
    private List<Long> liveInfoIds;

    private UpdateMemberLiveInformationRequest() {
    }

    public UpdateMemberLiveInformationRequest(final List<Long> liveInfoIds) {
        this.liveInfoIds = liveInfoIds;
    }

    public List<Long> getLiveInfoIds() {
        return liveInfoIds;
    }
}
