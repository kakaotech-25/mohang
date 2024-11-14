package moheng.planner.application;

import static moheng.fixture.TripScheduleFixtures.*;
import static moheng.fixture.TripFixture.*;
import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import moheng.config.slice.ServiceTestConfig;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.planner.domain.TripSchedule;
import moheng.planner.domain.TripScheduleRegistry;
import moheng.planner.domain.repository.TripScheduleRegistryRepository;
import moheng.planner.domain.repository.TripScheduleRepository;
import moheng.planner.dto.request.AddTripOnScheduleRequests;
import moheng.planner.dto.request.UpdateTripOrdersRequest;
import moheng.planner.dto.response.FindTripOnSchedule;
import moheng.planner.dto.response.FindTripsOnSchedule;
import moheng.planner.exception.AlreadyExistTripScheduleException;
import moheng.planner.exception.NoExistTripScheduleException;
import moheng.planner.exception.NoExistTripScheduleRegistryException;
import moheng.trip.domain.Trip;
import moheng.trip.domain.repository.TripRepository;
import moheng.trip.exception.NoExistTripException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

public class TripScheduleServiceTest extends ServiceTestConfig {
    @Autowired
    private TripScheduleService tripScheduleService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TripScheduleRepository tripScheduleRepository;

    @Autowired
    private TripScheduleRegistryRepository tripScheduleRegistryRepository;

    @DisplayName("여행 일정을 생성한다.")
    @Test
    void 여행_일정을_생성한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());

        // when, then
        assertDoesNotThrow(() -> {
            tripScheduleService.createTripSchedule(하온.getId(), 여행_일정1_생성_요청());
        });
    }

    @DisplayName("동일한 이름을 가진 멤버의 여행 일정이 이미 존재하면 예외가 발생한다.")
    @Test
    void 동일한_이름을_가진_멤버의_여행_일정이_이미_존재하면_예외가_발생한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleService.createTripSchedule(하온.getId(), 중복_여행_일정_생성_요청());

        // when, then
        assertThatThrownBy(() -> tripScheduleService.createTripSchedule(하온.getId(), 중복_여행_일정_생성_요청())
        ).isInstanceOf(AlreadyExistTripScheduleException.class);
    }

    @DisplayName("존재하지 않는 멤버의 여행 일정을 생성하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_멤버의_여행_일정을_생성하면_예외가_발생한다() {
        // given
        long 존재하지_않는_멤버_ID = -1L;

        // when, then
        assertThatThrownBy(() ->
                tripScheduleService.createTripSchedule(존재하지_않는_멤버_ID, 신규_여행_일정_생성_요청())
        ).isInstanceOf(NoExistMemberException.class);
    }

    @DisplayName("현재 여행지를 플래너 일정에 담는다.")
    @Test
    void 현재_여행지를_플래너_일정에_담는다() {
        // given
        Member 하온 = memberRepository.save(하온_기존()); Trip 여행지1 = tripRepository.save(여행지1_생성());
        TripSchedule 여행_일정1 = tripScheduleRepository.save(여행_일정1_생성(하온)); TripSchedule 여행_일정2 = tripScheduleRepository.save(여행_일정2_생성(하온));

        // when, then
        assertDoesNotThrow(() -> tripScheduleService.addCurrentTripOnPlannerSchedule(여행지1.getId(), new AddTripOnScheduleRequests(List.of(여행_일정1.getId(), 여행_일정2.getId()))));
    }

    @DisplayName("존재하지 않는 여행지를 플래너 일정에 담으려고하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_여행지를_플래너_일정에_담으려고하면_예외가_발생한다() {
        // given
        long 존재하지_않는_여행지_ID = -1L;
        Member 하온 = memberRepository.save(하온_기존());
        TripSchedule 여행_일정1 = tripScheduleRepository.save(new TripSchedule("여행 일정1", LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 2), 하온));
        TripSchedule 여행_일정2 = tripScheduleRepository.save(new TripSchedule("여행 일정2", LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 2), 하온));

        // when, then
        assertThatThrownBy(() -> tripScheduleService.addCurrentTripOnPlannerSchedule(존재하지_않는_여행지_ID, 플래너에_여행지_담기_요청(List.of(여행_일정1.getId(), 여행_일정2.getId()))))
                .isInstanceOf(NoExistTripException.class);
    }

    @DisplayName("존재하지 않는 여행 일정에 여행지를 담으려고하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_여행_일정에_여행지를_담으려고하면_예외가_발생한다() {
        // given
        long 존재하지_않는_여행_일정_ID = -1L;
        Member 하온 = memberRepository.save(하온_기존());
        Trip 여행지1 = tripRepository.save(여행지1_생성());

        // when, then
        assertThatThrownBy(() -> tripScheduleService.addCurrentTripOnPlannerSchedule(여행지1.getId(), new AddTripOnScheduleRequests(List.of(존재하지_않는_여행_일정_ID))))
                .isInstanceOf(NoExistTripScheduleException.class);
    }

    @DisplayName("세부 일정 정보를 찾는다.")
    @Test
    void 세부_일정_정보를_찾는다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        TripSchedule 여행_일정1 = tripScheduleRepository.save(여행_일정1_생성(하온));
        Trip 여행지1 = tripRepository.save(여행지1_생성()); Trip 여행지2 = tripRepository.save(여행지2_생성());

        tripScheduleRegistryRepository.save(여행_일정_등록_생성(여행지1, 여행_일정1));
        tripScheduleRegistryRepository.save(여행_일정_등록_생성(여행지2, 여행_일정1));

        // when
        FindTripsOnSchedule findTripsOnSchedule = tripScheduleService.findTripsOnSchedule(여행_일정1.getId());

        // then
        assertAll(() -> {
            assertThat(findTripsOnSchedule.getTripScheduleResponse().getScheduleName()).isEqualTo("일정1");
            assertThat(findTripsOnSchedule.getFindTripsOnSchedules()).hasSize(2);
        });
    }

    @DisplayName("세부 일정내의 여행지들의 정렬 순서를 바꾼다.")
    @Test
    void 세부_일정내의_여행지들의_정렬_순서를_바꾼다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        TripSchedule 여행_일정1 = tripScheduleRepository.save(여행_일정1_생성(하온));
        Trip 여행지1 = tripRepository.save(여행지1_생성()); Trip 여행지2 = tripRepository.save(여행지2_생성()); Trip 여행지3 = tripRepository.save(여행지3_생성());

        tripScheduleRegistryRepository.save(new TripScheduleRegistry(여행지1, 여행_일정1));
        tripScheduleRegistryRepository.save(new TripScheduleRegistry(여행지2, 여행_일정1));
        tripScheduleRegistryRepository.save(new TripScheduleRegistry(여행지3, 여행_일정1));

        // when
        UpdateTripOrdersRequest updateTripOrdersRequest = new UpdateTripOrdersRequest(List.of(3L, 1L, 2L));
        tripScheduleService.updateTripOrdersOnSchedule(여행_일정1.getId(), updateTripOrdersRequest);

        // then
        List<FindTripOnSchedule> findTripsOnSchedules = tripScheduleService.findTripsOnSchedule(여행_일정1.getId()).getFindTripsOnSchedules();

        assertAll(() -> {
            assertThat(findTripsOnSchedules).hasSize(3);
            assertThat(findTripsOnSchedules.get(0).getPlaceName()).isEqualTo("여행지3");
            assertThat(findTripsOnSchedules.get(1).getPlaceName()).isEqualTo("여행지1");
            assertThat(findTripsOnSchedules.get(2).getPlaceName()).isEqualTo("여행지2");
        });
    }

    @DisplayName("존재하지 않는 여행지의 정렬 순서를 바꾸려고하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_여행지의_정렬_순서를_바꾸려고하면_예외가_발생한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        TripSchedule 여행_일정1 = tripScheduleRepository.save(여행_일정1_생성(하온));

        // when, then
        assertThatThrownBy(() ->
                tripScheduleService.updateTripOrdersOnSchedule(여행_일정1.getId(), 유효하지_않은_여행지_정렬_순서_업데이트_요청())
        ).isInstanceOf(NoExistTripException.class);
    }

    @DisplayName("존재하지 않는 일정을 수정하려고 하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_일정을_수정하려고_하면_예외가_발생한다() {
        // given
        long 존재하지_않는_일정_ID = -1L;
        Member member = memberRepository.save(하온_기존());

        // when, then
        assertThatThrownBy(() ->
                tripScheduleService.updateTripOrdersOnSchedule(존재하지_않는_일정_ID, 여행지_정렬_순서_업데이트_요청(List.of(3L, 1L, 2L)))
        ).isInstanceOf(NoExistTripScheduleException.class);
    }

    @DisplayName("세부 일정내의 특정 여행지를 제거한다.")
    @Test
    void 세부_일정내의_특정_여행지를_제거한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        TripSchedule 여행_일정1 = tripScheduleRepository.save(여행_일정1_생성(하온));

        Trip 여행지1 = tripRepository.save(여행지1_생성());
        Trip 여행지2 = tripRepository.save(여행지2_생성());
        Trip 여행지3 = tripRepository.save(여행지3_생성());

        tripScheduleRegistryRepository.save(new TripScheduleRegistry(여행지1, 여행_일정1));
        tripScheduleRegistryRepository.save(new TripScheduleRegistry(여행지2, 여행_일정1));
        tripScheduleRegistryRepository.save(new TripScheduleRegistry(여행지3, 여행_일정1));

        // when
        tripScheduleService.deleteTripOnSchedule(여행지1.getId(), 여행지3.getId());

        // then
        int exptected = 2;
        assertThat(tripScheduleRegistryRepository.findAll()).hasSize(exptected);
    }

    @DisplayName("존재하지 않는 일정 여행지를 제거하려고 하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_일정_여행지를_제거하려고_하면_예외가_발생한다() {
        // given
        long 존재하지_않는_일정_ID = -1L;
        long 존재하지_않는_여행지_ID = -1L;

        // when, then
        assertThatThrownBy(() -> tripScheduleService.deleteTripOnSchedule(존재하지_않는_일정_ID, 존재하지_않는_여행지_ID))
                .isInstanceOf(NoExistTripScheduleRegistryException.class);
    }
}
