package moheng.fixture;

import moheng.liveinformation.domain.MemberLiveInformation;
import moheng.liveinformation.dto.UpdateMemberLiveInformationRequest;

import java.util.List;

public class MemberLiveInformationFixture {

    public static UpdateMemberLiveInformationRequest 멤버_생활정보_업데이트_요청(List<Long> liveInfoIds) {
        return new UpdateMemberLiveInformationRequest(liveInfoIds);
    }
}
