package moheng.liveinformation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.TripKeyword;
import moheng.trip.domain.Trip;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TripKeywordTest {

    @DisplayName("여행지의 키워드를 생성한다.")
    @Test
    void 여행지의_키워드를_생성한다() {
        assertDoesNotThrow(() -> new TripKeyword(new Trip("여행", "장소명", 1L, "설명", "이미지"), new Keyword("키워드")));
    }
}
