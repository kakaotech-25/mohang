package moheng.fixture;

import moheng.liveinformation.dto.response.FindMemberLiveInformationResponses;
import moheng.liveinformation.dto.response.LiveInfoResponse;
import moheng.liveinformation.dto.request.UpdateMemberLiveInformationRequest;

import java.util.List;

public class MemberLiveInformationFixture {

    public static UpdateMemberLiveInformationRequest 멤버_생활정보_업데이트_요청(List<Long> liveInfoIds) {
        return new UpdateMemberLiveInformationRequest(liveInfoIds);
    }

    public static FindMemberLiveInformationResponses 멤버가_선택한_생활정보_조회_응답() {
        return new FindMemberLiveInformationResponses(List.of(
                new LiveInfoResponse(1L, "생활정보1", true),
                new LiveInfoResponse(2L, "생활정보2", true),
                new LiveInfoResponse(3L, "생활정보3", false)));
    }
}
