package moheng.liveinformation.dto;

import java.util.List;

public class FindMemberLiveInformationResponses {
    private List<LiveInfoResponse> liveInfoResponses;

    private FindMemberLiveInformationResponses() {
    }

    public FindMemberLiveInformationResponses(final List<LiveInfoResponse> liveInfoResponses) {
        this.liveInfoResponses = liveInfoResponses;
    }

    public List<LiveInfoResponse> getLiveInfoResponses() {
        return liveInfoResponses;
    }
}
