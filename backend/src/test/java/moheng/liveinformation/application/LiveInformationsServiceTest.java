package moheng.liveinformation.application;

import static moheng.fixture.LiveInformationFixture.*;
import static moheng.fixture.TripFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import moheng.config.slice.ServiceTestConfig;
import moheng.config.TestConfig;
import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.repository.LiveInformationRepository;
import moheng.liveinformation.dto.FindAllLiveInformationResponse;
import moheng.liveinformation.dto.LiveInformationCreateRequest;
import moheng.liveinformation.exception.NoExistLiveInformationException;
import moheng.trip.domain.Trip;
import moheng.trip.domain.repository.TripRepository;
import moheng.trip.exception.NoExistTripException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class LiveInformationsServiceTest extends ServiceTestConfig {
    @Autowired
    private LiveInformationService liveInformationService;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private LiveInformationRepository liveInformationRepository;

    @DisplayName("생활정보를 저장한다.")
    @Test
    void 생활정보를_저장한다() {
        // given, when, then
        liveInformationService.save(생활정보1_생성());
    }

    @DisplayName("생활정보 이름으로 생활정보를 찾는다.")
    @Test
    void 생활정보_이름으로_생활정보를_찾는다() {
        // given
        LiveInformation 생활정보1 = liveInformationService.save(생활정보1_생성());

        // when, then
       assertDoesNotThrow(() -> liveInformationService.findByName(생활정보1.getName()));
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
                liveInformationService.createLiveInformation(생활정보_생성_요청("생활정보")));
    }

    @DisplayName("모든 생활정보를 조회한다.")
    @Test
    void 모든_생활정보를_조회한다() {
        // given
        liveInformationRepository.save(생활정보1_생성());
        liveInformationRepository.save(생활정보2_생성());
        liveInformationRepository.save(생활정보3_생성());

        // when
        FindAllLiveInformationResponse actual = liveInformationService.findAllLiveInformation();

        // then
        assertEquals(actual.getLiveInformationResponses().size(), 3);
    }

    @DisplayName("여행지의 생활정보를 생성한다.")
    @Test
    void 여행지의_생활정보를_생성한다() {
        // given
        LiveInformation 생활정보1 = liveInformationRepository.save(생활정보1_생성());
        Trip 여행지1 = tripRepository.save(여행지1_생성());

        // when, then
        assertDoesNotThrow(() -> liveInformationService.createTripLiveInformation(여행지1.getId(), 생활정보1.getId()));
    }

    @DisplayName("존재하지 않는 여행지의 생활정보를 생성하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_여행지의_생활정보를_생성하면_예외가_발생한다() {
        // given
        long 유효하지_않은_여행지_ID = -1L;
        LiveInformation liveInformation = liveInformationRepository.save(생활정보1_생성());

        // when, then
        assertThatThrownBy(() -> liveInformationService.createTripLiveInformation(유효하지_않은_여행지_ID, liveInformation.getId()))
                .isInstanceOf(NoExistTripException.class);
    }

    @DisplayName("존재하지 않는 생활정보로 여행지의 생활정보를 생성하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_생활정보로_여행지의_생활정보를_생성하면_예외가_발생한다() {
        // given
        long 존재하지_않는_생활정보_ID = -1L;
        Trip 여행지1 = tripRepository.save(여행지1_생성());

        // when, then
        assertThatThrownBy(() -> liveInformationService.createTripLiveInformation(여행지1.getId(), 존재하지_않는_생활정보_ID))
                .isInstanceOf(NoExistLiveInformationException.class);
    }
}
