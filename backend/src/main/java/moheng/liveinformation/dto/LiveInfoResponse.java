package moheng.liveinformation.dto;

public class LiveInfoResponse {
    private Long liveInfoId;
    private String name;
    private boolean contain;

    private LiveInfoResponse() {
    }

    public LiveInfoResponse(Long liveInfoId, String name, boolean contain) {
        this.liveInfoId = liveInfoId;
        this.name = name;
        this.contain = contain;
    }

    public Long getLiveInfoId() {
        return liveInfoId;
    }

    public String getName() {
        return name;
    }

    public boolean getContain() {
        return contain;
    }
}
