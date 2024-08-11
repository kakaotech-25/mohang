package moheng.liveinformation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import moheng.liveinformation.exception.LiveInfoNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LiveInformationTest {
    @DisplayName("생활정보를 생성한다.")
    @Test
    void 생활정보를_생성한다() {
        // given, when, then
        assertDoesNotThrow(() -> new LiveInformation("생활정보1"));
    }

    @DisplayName("생활정보 이름의 길이가 올바르지 않다면 예외가 발생한다.")
    @Test
    void 생활정보_이름의_길이가_올바르지_않다면_예외가_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> new LiveInformation("")).isInstanceOf(LiveInfoNameException.class);
    }
}
