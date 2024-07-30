package moheng.member.domain;

import moheng.auth.exception.NoMatchingSocialTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


public class SocialTypeTest {

    @DisplayName("카카오 소셜 계정 타입을 찾는다.")
    @Test
    void 카카오_소셜_계정_타입을_찾는다() {
        // given, when, then
        assertEquals(SocialType.KAKAO, SocialType.findByName("KAKAO"));
        assertEquals(SocialType.KAKAO, SocialType.findByName("kakao"));
    }

    @DisplayName("구글 소셜 계정 타입을 찾는다.")
    @Test
    void 구글_소셜_계정_타입을_찾는다() {
        // given, when, then
        assertEquals(SocialType.GOOGLE, SocialType.findByName("GOOGLE"));
        assertEquals(SocialType.GOOGLE, SocialType.findByName("google"));
    }

    @DisplayName("입력에 매핑되는 소셜 타입이 없다면 예외가 발생한다.")
    @Test
    void 입력에_매핑되는_소셜_타입이_없다면_예외가_발생한다() {
        // given
        String providerName = null;

        // when, then
        assertThatThrownBy(() -> SocialType.findByName(providerName))
                .isInstanceOf(NoMatchingSocialTypeException.class);
    }

    @DisplayName("일치하는 소셜 타입이 있다면 참을 리턴한다.")
    @Test
    void 일치하는_소셜_타입이_있다면_거짓을_리턴한다() {
        // given, when, then
        assertThat(SocialType.isMatches(SocialType.KAKAO)).isTrue();
        assertThat(SocialType.isMatches(SocialType.GOOGLE)).isTrue();

    }

    @DisplayName("일치하는 소셜 타입이 없다면 거짓을 리턴한다.")
    @Test
    void 일치하는_소셜_타입이_없다면_거짓을_리턴한다() {
        // given, when, then
        assertThat(SocialType.isMatches(null)).isFalse();
    }
}
