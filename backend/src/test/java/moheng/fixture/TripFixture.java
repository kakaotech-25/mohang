package moheng.fixture;

import moheng.member.domain.GenderType;
import moheng.member.dto.request.SignUpProfileRequest;
import moheng.trip.dto.TripCreateRequest;

import java.time.LocalDate;

public class TripFixture {
    // 여행지 생성 요청
    public static TripCreateRequest 롯데월드_여행지_생성_요청() {
        return new TripCreateRequest("롯데월드", "서울특별시 송파구", 1L, "롯데월드 관련 설명", "https://lotte-world.png");
    }
}
