package moheng.auth.domain;

import static org.junit.jupiter.api.Assertions.*;

import moheng.auth.infrastructure.KakaoOAuthClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KakaoOAuthClientTest {
    @Autowired
    private KakaoOAuthClient kakaoOAuthClient;

    @DisplayName("OAuth 클라이언트 식별자가 동일하다면 참을 리턴한다.")
    @Test
    void OAuth_클라이언트_식별자가_동일하다면_참을_리턴한다() {
        // given, when, then
        assertTrue(kakaoOAuthClient.isSame("KAKAO"));
    }
}
