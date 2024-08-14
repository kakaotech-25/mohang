package moheng.keyword.domain;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberKeywordTest {

    @DisplayName("멤버의 키워드를 생성한다.")
    @Test
    void 멤버의_키워드를_생성한다() {
        assertDoesNotThrow(() -> new MemberKeyword(하온_기존(), new Keyword("키워드")));
    }
}
