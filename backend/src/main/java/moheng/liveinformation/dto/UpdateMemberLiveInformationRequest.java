package moheng.liveinformation.dto;

import java.util.List;

public class UpdateMemberLiveInformationRequest {
    private List<Long> contentIds;

    private UpdateMemberLiveInformationRequest() {
    }

    public UpdateMemberLiveInformationRequest(final List<Long> contentIds) {
        this.contentIds = contentIds;
    }

    public List<Long> getContentIds() {
        return contentIds;
    }
}
