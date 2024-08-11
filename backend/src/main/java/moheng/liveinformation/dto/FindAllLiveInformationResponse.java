package moheng.liveinformation.dto;

import moheng.liveinformation.domain.LiveInformation;

import java.util.List;
import java.util.stream.Collectors;

public class FindAllLiveInformationResponse {
    private List<LiveInformationResponse> liveInformationResponses;

    public FindAllLiveInformationResponse(final List<LiveInformation> liveInformations) {
        this.liveInformationResponses = toResponses(liveInformations);
    }

    private List<LiveInformationResponse> toResponses(final List<LiveInformation> liveInformations) {
        return liveInformations.stream()
                .map(LiveInformationResponse::new)
                .collect(Collectors.toList());
    }

    public List<LiveInformationResponse> getLiveInformationResponses() {
        return liveInformationResponses;
    }
}
