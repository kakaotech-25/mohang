package moheng.liveinformation.dto;

public class LiveInfoResponse {
    private Long liveInfoId;
    private String name;
    private boolean isContain;

    private LiveInfoResponse() {
    }

    public LiveInfoResponse(Long liveInfoId, String name, boolean isContain) {
        this.liveInfoId = liveInfoId;
        this.name = name;
        this.isContain = isContain;
    }

    public Long getLiveInfoId() {
        return liveInfoId;
    }

    public String getName() {
        return name;
    }

    public boolean isContain() {
        return isContain;
    }
}
