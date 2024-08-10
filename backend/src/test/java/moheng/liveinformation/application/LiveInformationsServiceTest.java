package moheng.liveinformation.application;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import moheng.config.ServiceTestConfig;
import moheng.config.TestConfig;
import moheng.liveinformation.domain.LiveInformation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class LiveInformationsServiceTest extends ServiceTestConfig {
    @Autowired
    private LiveInformationService liveInformationService;

    @DisplayName("생활정보 이름으로 생활정보를 찾는다.")
    @Test
    void 생활정보_이름으로_생활정보를_찾는다() {
        // given
        String liveInformationName = "영유아가족";
        liveInformationService.save(new LiveInformation(liveInformationName));

        // when, then
       assertDoesNotThrow(() -> liveInformationService.findByName(liveInformationName));
    }
}
