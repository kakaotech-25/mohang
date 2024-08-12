package moheng.fixture;

import moheng.liveinformation.dto.UpdateMemberLiveInformationRequest;
import moheng.member.dto.request.SignUpInterestTripsRequest;

import java.util.List;

public class MemberLiveInfoFixture {
    public static UpdateMemberLiveInformationRequest 멤버_생활정보_수정_요청() {
        return new UpdateMemberLiveInformationRequest(List.of(1L, 2L, 3L));
    }
}
