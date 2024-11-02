package moheng.recommendtrip.domain.filterinfo;

public class PreferredLocationsFilterInfo implements FilterStandardInfo {
    private final Long memberId;

    public PreferredLocationsFilterInfo(final Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public long getInfo() {
        return memberId;
    }
}
