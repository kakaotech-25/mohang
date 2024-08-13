package moheng.keyword.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import moheng.keyword.exception.KeywordNameLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class KeywordTest {

    @DisplayName("키워드를 생성한다.")
    @Test
    void 키워드를_생성한다() {
        assertDoesNotThrow(() -> new Keyword("키워드"));
    }

    @DisplayName("키워드 이름의 길이가 유효하지 않다면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "일이삼자오육칠팔구십 일이삼자오육칠팔구십 일이삼자오육칠팔구십 일이삼자오육칠팔구십 일이삼자오육칠팔구십 일이삼자오육칠팔구십 일이삼자오육칠팔구십 일이삼자오육칠팔구십 일이삼자오육칠팔구십 일이삼자오육칠팔구십 ",
            "",
            "a",
            "오"
    })
    void 키워드_이름의_길이가_2자_이하라면_예외가_발생한다(final String name) {
        assertThatThrownBy(() -> new Keyword(name))
                .isInstanceOf(KeywordNameLengthException.class);
    }
}
