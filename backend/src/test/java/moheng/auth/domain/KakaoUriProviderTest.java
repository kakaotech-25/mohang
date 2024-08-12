package moheng.auth.domain;

import static org.junit.jupiter.api.Assertions.*;

import moheng.auth.domain.oauth.KakaoUriProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KakaoUriProviderTest {
    @Autowired
    private KakaoUriProvider kakaoUriProvider;

    @DisplayName("OAuth URI 제공처 식별자가 동일하다면 참을 리턴한다.")
    @Test
    void OAuth_URI_제공처_식별자가_동일하다면_참을_리턴한다() {
        assertTrue(kakaoUriProvider.isSame("KAKAO"));
    }
}
