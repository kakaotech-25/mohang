package moheng.auth.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


import moheng.auth.domain.token.RenewalToken;
import moheng.auth.exception.NoExistMemberTokenException;
import moheng.member.exception.NoExistSocialTypeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RenewalTokenTest {

    @DisplayName("전달받은 리프레시 토큰과 회원의 리프레시 토큰이 다르면 예외가 발생한다.")
    @Test
    void 전달받은_리프레시_토큰과_회원의_리프레시_토큰이_다르면_예외가_발생한다() {
        // given
        RenewalToken renewalToken = new RenewalToken("access-token", "refresh-token");

        // when, then
        assertThatThrownBy(() -> renewalToken.validateHasSameRefreshToken("other-token"))
                .isInstanceOf(NoExistMemberTokenException.class);
    }
}
