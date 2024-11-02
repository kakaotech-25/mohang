package moheng.fixture;

import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.dto.FindAllLiveInformationResponse;
import moheng.liveinformation.dto.LiveInformationCreateRequest;

import java.util.List;

public class LiveInformationFixture {
    // 생활정보 생성
    public static LiveInformation 생활정보1_생성() {
        return new LiveInformation("생활정보1");
    }

    public static LiveInformation 생활정보2_생성() {
        return new LiveInformation("생활정보2");
    }

    public static LiveInformation 생활정보3_생성() {
        return new LiveInformation("생활정보3");
    }

    // 생활정보 생성 요청
    public static LiveInformationCreateRequest 생활정보_생성_요청(String name) {
        return new LiveInformationCreateRequest(name);
    }

    // 모든 키워드 조회 응답
    public static FindAllLiveInformationResponse 모든_키워드_조회_응답() {
        return new FindAllLiveInformationResponse(
                List.of(new LiveInformation(1L, "생활정보1"),
                        new LiveInformation(2L, "생활정보2"),
                        new LiveInformation(3L, "생활정보3")
                ));
    }
}
