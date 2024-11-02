package moheng.member.dto.request;

import java.util.List;

public class SignUpInterestTripsRequest {
    private List<Long> contentIds;

    private SignUpInterestTripsRequest() {
    }

    public SignUpInterestTripsRequest(final List<Long> contentIds) {
        this.contentIds = contentIds;
    }

    public List<Long> getContentIds() {
        return contentIds;
    }
}
