package moheng.fixture;

import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.dto.LiveInformationCreateRequest;

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
}
