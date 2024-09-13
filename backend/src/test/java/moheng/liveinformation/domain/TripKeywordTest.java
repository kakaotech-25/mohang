package moheng.liveinformation.domain;

import static moheng.fixture.TripFixture.여행지1_생성;
import static moheng.fixture.KeywordFixture.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import moheng.keyword.domain.TripKeyword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TripKeywordTest {

    @DisplayName("여행지의 키워드를 생성한다.")
    @Test
    void 여행지의_키워드를_생성한다() {
        // given, when, then
        assertDoesNotThrow(() -> new TripKeyword(여행지1_생성(), 키워드1_생성()));
    }
}
