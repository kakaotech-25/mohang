package moheng.recommendtrip.domain.preferredlocation;

import java.util.Map;

public interface PreferredLocationsFinder {
    Map<Long, Long> findPreferredLocations(final long memberId);
}
