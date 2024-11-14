package moheng.liveinformation.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class UpdateMemberLiveInformationRequest {

    @NotEmpty(message = "업데이트 할 생활정보는 비어있을 수 없습니다.")
    private List<Long> liveInfoIds;

    private UpdateMemberLiveInformationRequest() {
    }

    public UpdateMemberLiveInformationRequest(final List<Long> liveInfoIds) {
        this.liveInfoIds = liveInfoIds;
    }

    public List<Long> getLiveInfoIds() {
        return liveInfoIds;
    }
}
