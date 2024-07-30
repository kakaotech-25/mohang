package moheng.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class SocialTypeTest {

    @DisplayName("카카오 소셜 계정 타입을 찾는다.")
    @Test
    void 카카오_소셜_계정_타입을_찾는다() {
        assertEquals(SocialType.KAKAO, SocialType.findByName("KAKAO"));
        assertEquals(SocialType.KAKAO, SocialType.findByName("kakao"));
    }

    @DisplayName("구글 소셜 계정 타입을 찾는다.")
    @Test
    void 구글_소셜_계정_타입을_찾는다() {
        assertEquals(SocialType.GOOGLE, SocialType.findByName("GOOGLE"));
        assertEquals(SocialType.GOOGLE, SocialType.findByName("google"));
    }

    @DisplayName("일치하는 소셜 타입이 없다면 거짓을 리턴한다.")
    @Test
    void 일치하는_소셜_타입이_없다면_거짓을_리턴한다() {
        assertThat(SocialType.isMatches(null)).isFalse();
    }
}
