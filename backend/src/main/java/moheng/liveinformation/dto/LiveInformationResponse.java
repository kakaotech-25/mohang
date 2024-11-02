package moheng.liveinformation.dto;

import moheng.liveinformation.domain.LiveInformation;

public class LiveInformationResponse {
    private Long id;
    private String name;

    private LiveInformationResponse() {
    }

    public LiveInformationResponse(final LiveInformation liveInformation) {
        this.id = liveInformation.getId();
        this.name = liveInformation.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
