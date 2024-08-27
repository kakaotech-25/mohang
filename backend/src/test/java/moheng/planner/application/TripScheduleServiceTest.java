package moheng.planner.application;

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
import moheng.planner.domain.TripScheduleRegistryRepository;
import moheng.planner.domain.TripScheduleRepository;
import moheng.planner.dto.CreateTripScheduleRequest;
import moheng.planner.dto.FindTripOnSchedule;
import moheng.planner.dto.FindTripsOnSchedule;
import moheng.planner.dto.UpdateTripOrdersRequest;
import moheng.planner.exception.AlreadyExistTripScheduleException;
import moheng.planner.exception.NoExistTripScheduleException;
import moheng.trip.domain.Trip;
import moheng.trip.domain.TripRepository;
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
        Member member = memberRepository.save(하온_기존());

        // when, then
        assertDoesNotThrow(() -> {
            tripScheduleService.createTripSchedule(
                    member.getId(),
                    new CreateTripScheduleRequest("일정1", LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 2))
            );
        });
    }

    @DisplayName("동일한 이름을 가진 멤버의 여행 일정이 이미 존재하면 예외가 발생한다.")
    @Test
    void 동일한_이름을_가진_멤버의_여행_일정이_이미_존재하면_예외가_발생한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripScheduleService.createTripSchedule(member.getId(),
                new CreateTripScheduleRequest("기존일정", LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 2)));

        // when, then
        assertThatThrownBy(() ->
            tripScheduleService.createTripSchedule(member.getId(),
                    new CreateTripScheduleRequest("기존일정", LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 2))
        )).isInstanceOf(AlreadyExistTripScheduleException.class);
    }

    @DisplayName("존재하지 않는 멤버의 여행 일정을 생성하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_멤버의_여행_일정을_생성하면_예외가_발생한다() {
        // given
        long invalidMemberId = -1L;

        // when, then
        assertThatThrownBy(() ->
                tripScheduleService.createTripSchedule(invalidMemberId,
                        new CreateTripScheduleRequest("신규일정", LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 2))
                )).isInstanceOf(NoExistMemberException.class);
    }

    @DisplayName("현재 여행지를 플래너 일정에 담는다.")
    @Test
    void 현재_여행지를_플래너_일정에_담는다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip trip = tripRepository.save(new Trip("여행지", "장소명", 1L, "설명", "https://image.com"));
        TripSchedule tripSchedule = tripScheduleRepository.save(new TripSchedule("여행 일정", LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 2), member));

        // when, then
        assertDoesNotThrow(() -> tripScheduleService.addCurrentTripOnPlannerSchedule(trip.getId(), tripSchedule.getId()));
    }

    @DisplayName("존재하지 않는 여행지를 플래너 일정에 담으려고하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_여행지를_플래너_일정에_담으려고하면_예외가_발생한다() {
        // given
        long invalidTripId = -1L;
        Member member = memberRepository.save(하온_기존());
        TripSchedule tripSchedule = tripScheduleRepository.save(new TripSchedule("여행 일정", LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 2), member));

        // when, then
        assertThatThrownBy(() -> tripScheduleService.addCurrentTripOnPlannerSchedule(invalidTripId, tripSchedule.getId()))
                .isInstanceOf(NoExistTripException.class);
    }

    @DisplayName("존재하지 않는 여행 일정에 여행지를 담으려고하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_여행_일정에_여행지를_담으려고하면_예외가_발생한다() {
        // given
        long invalidScheduleId = -1L;
        Member member = memberRepository.save(하온_기존());
        Trip trip = tripRepository.save(new Trip("여행지", "장소명", 1L, "설명", "https://image.com"));

        // when, then
        assertThatThrownBy(() -> tripScheduleService.addCurrentTripOnPlannerSchedule(trip.getId(), invalidScheduleId))
                .isInstanceOf(NoExistTripScheduleException.class);
    }

    @DisplayName("세부 일정 정보를 찾는다.")
    @Test
    void 세부_일정_정보를_찾는다() {
        // given
        Member member = memberRepository.save(하온_기존());
        TripSchedule tripSchedule = tripScheduleRepository.save(new TripSchedule("여행 일정", LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 2), member));

        Trip trip1 = tripRepository.save(new Trip("여행지1", "장소명", 1L, "설명", "https://image.com", 126.3307690830, 36.5309210243));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "장소명", 1L, "설명", "https://image.com", 226.3307690830, 46.5309210243));

        tripScheduleRegistryRepository.save(new TripScheduleRegistry(trip1, tripSchedule));
        tripScheduleRegistryRepository.save(new TripScheduleRegistry(trip2, tripSchedule));

        // when
        FindTripsOnSchedule findTripsOnSchedule = tripScheduleService.findTripsOnSchedule(tripSchedule.getId());

        // then
        assertAll(() -> {
            assertThat(findTripsOnSchedule.getTripScheduleResponse().getScheduleName()).isEqualTo("여행 일정");
            assertThat(findTripsOnSchedule.getFindTripsOnSchedules()).hasSize(2);
            assertThat(findTripsOnSchedule.getFindTripsOnSchedules().get(0).getCoordinateX()).isEqualTo(126.3307690830);
            assertThat(findTripsOnSchedule.getFindTripsOnSchedules().get(0).getCoordinateY()).isEqualTo(36.5309210243);
        });
    }

    @DisplayName("세부 일정내의 여행지들의 정렬 순서를 바꾼다.")
    @Test
    void 세부_일정내의_여행지들의_정렬_순서를_바꾼다() {
        // given
        Member member = memberRepository.save(하온_기존());
        TripSchedule tripSchedule = tripScheduleRepository.save(new TripSchedule("여행 일정", LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 2), member));

        Trip trip1 = tripRepository.save(new Trip("여행지1", "장소명", 1L, "설명1", "https://image.com", 126.3307690830, 36.5309210243));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "장소명", 2L, "설명2", "https://image.com", 226.3307690830, 46.5309210243));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "장소명", 3L, "설명3", "https://image.com", 326.3307690830, 56.5309210243));

        tripScheduleRegistryRepository.save(new TripScheduleRegistry(trip1, tripSchedule));
        tripScheduleRegistryRepository.save(new TripScheduleRegistry(trip2, tripSchedule));
        tripScheduleRegistryRepository.save(new TripScheduleRegistry(trip3, tripSchedule));

        // when
        UpdateTripOrdersRequest updateTripOrdersRequest = new UpdateTripOrdersRequest(List.of(3L, 1L, 2L));
        tripScheduleService.updateTripOrdersOnSchedule(tripSchedule.getId(), updateTripOrdersRequest);

        // then
        List<FindTripOnSchedule> findTripsOnSchedules = tripScheduleService.findTripsOnSchedule(tripSchedule.getId()).getFindTripsOnSchedules();

        assertAll(() -> {
            assertThat(findTripsOnSchedules).hasSize(3);
            assertThat(findTripsOnSchedules.get(0).getPlaceName()).isEqualTo("여행지3");
            assertThat(findTripsOnSchedules.get(1).getPlaceName()).isEqualTo("여행지1");
            assertThat(findTripsOnSchedules.get(2).getPlaceName()).isEqualTo("여행지2");
        });
    }
}
