package moheng.member.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class SignUpLiveInfoRequest {

    @NotEmpty(message = "등록할 일정 리스트는 비어있을 수 없습니다.")
    private List<String> liveInfoNames;

    private SignUpLiveInfoRequest() {
    }

    public SignUpLiveInfoRequest(final List<String> liveInfoNames) {
        this.liveInfoNames = liveInfoNames;
    }

    public List<String> getLiveInfoNames() {
        return liveInfoNames;
    }
}
