package moheng.recommendtrip.domain.filterinfo;

public class LiveInformationFilterInfo implements FilterStandardInfo {
    private final Long tripId;

    public LiveInformationFilterInfo(final Long tripId) {
        this.tripId = tripId;
    }

    @Override
    public long getInfo() {
        return tripId;
    }
}
