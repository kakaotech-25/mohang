package moheng.liveinformation.application;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import moheng.config.ServiceTestConfig;
import moheng.config.TestConfig;
import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.dto.LiveInformationCreateRequest;
import moheng.liveinformation.exception.NoExistLiveInformationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class LiveInformationsServiceTest extends ServiceTestConfig {
    @Autowired
    private LiveInformationService liveInformationService;

    @DisplayName("생활정보를 저장한다.")
    @Test
    void 생활정보를_저장한다() {
        // given, when, then
        liveInformationService.save(new LiveInformation("생활정보 이름"));
    }

    @DisplayName("생활정보 이름으로 생활정보를 찾는다.")
    @Test
    void 생활정보_이름으로_생활정보를_찾는다() {
        // given
        String liveInformationName = "영유아가족";
        liveInformationService.save(new LiveInformation(liveInformationName));

        // when, then
       assertDoesNotThrow(() -> liveInformationService.findByName(liveInformationName));
    }

    @DisplayName("생활정보 이름에 매칭되는 생활정보가 없다면 예외가 발생한다.")
    @Test
    void 생활정보_이름에_매칭되는_생활정보가_없다면_예외가_발생한다() {
        // given, when, them
        assertThatThrownBy(() -> liveInformationService.findByName("없는 생활정보"))
                .isInstanceOf(NoExistLiveInformationException.class);
    }

    @DisplayName("생활정보를 생성한다.")
    @Test
    void 생활정보를_생성한다() {
        // given, when, then
        assertDoesNotThrow(() ->
                liveInformationService.createLiveInformation(new LiveInformationCreateRequest("생활정보1")));
    }
}
