package moheng.liveinformation.domain;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.TripKeyword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TripKeywordTest {

    @DisplayName("여행지의 키워드를 생성한다.")
    @Test
    void 여행지의_키워드를_생성한다() {
        assertDoesNotThrow(() -> new TripKeyword(하온_기존(), new Keyword("키워드")));
    }
}
