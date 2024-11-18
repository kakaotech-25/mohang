package moheng.planner.application;

import static moheng.fixture.TripFixture.*;
import static moheng.fixture.MemberFixtures.*;
import static moheng.fixture.TripScheduleFixtures.*;
import static moheng.fixture.TripScheduleFixtures.이번달_공개_여행_일정3_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.ServiceTestConfig;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.planner.domain.TripSchedule;
import moheng.planner.domain.TripScheduleRegistry;
import moheng.planner.domain.repository.TripScheduleRegistryRepository;
import moheng.planner.domain.repository.TripScheduleRepository;
import moheng.planner.dto.request.FindPlannerOrderByDateBetweenRequest;
import moheng.planner.dto.request.FindPublicSchedulesForRangeRequest;
import moheng.planner.dto.request.FindSchedulesByNameRequest;
import moheng.planner.dto.request.FindSchedulesNameResponses;
import moheng.planner.dto.response.*;
import moheng.planner.exception.AlreadyExistTripScheduleException;
import moheng.planner.exception.InvalidDateSequenceException;
import moheng.planner.exception.NoExistTripScheduleException;
import moheng.trip.domain.Trip;
import moheng.trip.domain.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

public class PlannerServiceTest extends ServiceTestConfig {
    @Autowired
    private PlannerService plannerService;

    @Autowired
    private TripScheduleRepository tripScheduleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TripScheduleRegistryRepository tripScheduleRegistryRepository;

    @Autowired
    private TripRepository tripRepository;

    @DisplayName("플래너 여행 일정을 최신순인 생성날짜를 기준으로 내림차순 정렬한다.")
    @Test
    void 플래너_여행_일정을_최신순인_생성날짜를_기준으로_내림차순_정렬한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(여행_일정1_생성(하온)); tripScheduleRepository.save(여행_일정2_생성(하온));
        tripScheduleRepository.save(여행_일정3_생성(하온)); tripScheduleRepository.save(여행_일정4_생성(하온));

        // when
        FindPlannerOrderByRecentResponse response = plannerService.findPlannerOrderByRecent(하온.getId());
        List<TripScheduleResponse> tripScheduleResponses = response.getTripScheduleResponses();

        // then
        assertAll(() -> {
            assertThat(tripScheduleResponses.get(0).getScheduleName()).isEqualTo("일정4");
            assertThat(tripScheduleResponses.get(3).getScheduleName()).isEqualTo("일정1");
            assertThat(tripScheduleResponses.size()).isEqualTo(4);
        });
    }

    @DisplayName("플래너 여행 일정을 날짜순인 시작날짜를 기준으로 오름차순 정렬한다.")
    @Test
    void 플래너_여행_일정을_날짜순인_시작날짜를_기준으로_오름차순_정렬한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(여행_일정1_생성(하온)); tripScheduleRepository.save(여행_일정2_생성(하온));
        tripScheduleRepository.save(여행_일정3_생성(하온)); tripScheduleRepository.save(여행_일정4_생성(하온));

        // when
        FindPlannerOrderByDateResponse response = plannerService.findPlannerOrderByDateAsc(하온.getId());
        List<TripScheduleResponse> tripScheduleResponses = response.getTripScheduleResponses();

        // then
        assertAll(() -> {
            assertThat(tripScheduleResponses.get(0).getScheduleName()).isEqualTo("일정1");
            assertThat(tripScheduleResponses.get(3).getScheduleName()).isEqualTo("일정4");
            assertThat(tripScheduleResponses.size()).isEqualTo(4);
        });
    }

    @DisplayName("플래너 여행 일정을 이름순으로 오름차순 정렬한다.")
    @Test
    void 플래너_여행_일정을_이름순으로_오름차순_정렬한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(여행_일정_가_생성(하온)); tripScheduleRepository.save(여행_일정_나_생성(하온));
        tripScheduleRepository.save(여행_일정_다_생성(하온)); tripScheduleRepository.save(여행_일정_라_생성(하온));

        // when
        FindPLannerOrderByNameResponse response = plannerService.findPlannerOrderByName(하온.getId());
        List<TripScheduleResponse> tripScheduleResponses = response.getTripScheduleResponses();

        // then
        assertAll(() -> {
            assertThat(tripScheduleResponses.get(0).getScheduleName()).isEqualTo("가 일정");
            assertThat(tripScheduleResponses.get(3).getScheduleName()).isEqualTo("라 일정");
            assertThat(tripScheduleResponses.size()).isEqualTo(4);
        });
    }

    @DisplayName("존재하지 않는 멤버의 플래너를 찾으면 예외가 발생한다.")
    @Test
    void 존재하지_않는_멤버의_플래너를_찾으면_예외가_발생한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(여행_일정_가_생성(하온)); tripScheduleRepository.save(여행_일정_나_생성(하온));

        long 존재하지_않는_멤버_ID = -1L;

        // when, then
        assertThatThrownBy(() -> plannerService.findPlannerOrderByName(존재하지_않는_멤버_ID))
                .isInstanceOf(NoExistMemberException.class);
    }

    @DisplayName("여행 일정을 수정한다.")
    @Test
    void 여행_일정을_수정한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(여행_일정_가_생성(하온));

        assertDoesNotThrow(() -> plannerService.updateTripSchedule(하온.getId(), 여행_일정_수정_요청()));
    }

    @DisplayName("존재하지 않는 회원의 여행 일정을 수정하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_회원의_여행_일정을_수정하면_예외가_발생한다() {
        // given
        long 유효하지_않은_멤버_ID = -1L;

        // when, then
        assertThatThrownBy(() -> plannerService.updateTripSchedule(유효하지_않은_멤버_ID, 여행_일정_수정_요청()))
                .isInstanceOf(NoExistMemberException.class);
    }

    @DisplayName("존재하지 않는 여행 일정을 수정하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_여행_일정을_수정하면_예외가_발생한다() {
        // given
        long 유효하지_않은_일정_ID = -1L;
        Member 하온 = memberRepository.save(하온_기존());

        // when, then
        assertThatThrownBy(() -> plannerService.updateTripSchedule(하온.getId(), 여행_일정_수정_요청()))
                .isInstanceOf(NoExistTripScheduleException.class);
    }

    @DisplayName("여행 일정의 이름이 변경된 경우 중복 이름을 체크하고, 중복이 발생했다면 예외가 발생한다.")
    @Test
    void 여행_일정의_이름이_변경된_경우_중복_이름을_체크하고_중복이_발생했다면_예외가_발생한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(여행_일정1_생성(하온));
        tripScheduleRepository.save(여행_일정_중복_생성(하온));

        // when, then
        assertThatThrownBy(() -> plannerService.updateTripSchedule(하온.getId(), 중복_여행_일정_수정_요청()))
                .isInstanceOf(AlreadyExistTripScheduleException.class);
    }

    @DisplayName("여행 일정을 삭제한다.")
    @Test
    void 여행_일정을_삭제한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        TripSchedule 여행_일정 = tripScheduleRepository.save(여행_일정_가_생성(하온));

        // when
        plannerService.removeTripSchedule(여행_일정.getId());

        // then
        assertThat(tripScheduleRepository.findAll()).hasSize(0);
    }

    @DisplayName("존재하지 않는 여행 일정 삭제를 시도하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_여행_일정_삭제를_시도하면_에외가_발생한다() {
        // given
        long 존재하지_않는_여행_일정_ID = -1L;

        // when, then
        assertThatThrownBy(() -> plannerService.removeTripSchedule(존재하지_않는_여행_일정_ID))
                .isInstanceOf(NoExistTripScheduleException.class);
    }

    @DisplayName("여행 일정을 삭제하면 그 안의 여행지들도 함께 삭제된다.")
    @Test
    void 여행_일정을_삭제하면_그_안의_여행지들도_함께_삭제된다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        Trip 여행지1 = tripRepository.save(여행지1_생성());
        Trip 여행지2 = tripRepository.save(여행지2_생성());
        TripSchedule 여행_일정1_생성 = tripScheduleRepository.save(여행_일정1_생성(하온));
        tripScheduleRegistryRepository.save(new TripScheduleRegistry(여행지1, 여행_일정1_생성));
        tripScheduleRegistryRepository.save(new TripScheduleRegistry(여행지2, 여행_일정1_생성));

        // when
        plannerService.removeTripSchedule(여행_일정1_생성.getId());

        // then
        assertAll(() -> {
            assertThat(tripScheduleRepository.findAll()).hasSize(0);
            assertThat(tripScheduleRegistryRepository.findAll()).hasSize(0);
        });
    }

    @DisplayName("플래너 여행 일정을 주어진 범위 내에서 생성날짜를 기준으로 내림차순 정렬한다.")
    @Test
    void 플래너_여행_일정을_주어진_범위_내에서_생성날짜를_기준으로_내림차순_정렬한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(여행_일정1_생성(하온)); tripScheduleRepository.save(여행_일정2_생성(하온));
        tripScheduleRepository.save(여행_일정3_생성(하온)); tripScheduleRepository.save(여행_일정4_생성(하온));

        LocalDate 시작날짜 = LocalDate.now().minusDays(1);
        LocalDate 종료날짜 = LocalDate.now().plusDays(1);

        FindPlannerOrderByDateBetweenRequest 플래너_조회_요청 = new FindPlannerOrderByDateBetweenRequest(시작날짜, 종료날짜);

        // when
        FindPlannerOrderByDateBetweenResponse 플래너_조회_응답 = plannerService.findPlannerOrderByDateAndRange(하온.getId(), 플래너_조회_요청);

        // then
        assertAll(() -> {
            assertThat(플래너_조회_응답.getTripScheduleResponses()).hasSize(4);
            assertThat(플래너_조회_응답.getTripScheduleResponses().get(0).getScheduleName()).isEqualTo("일정4");
            assertThat(플래너_조회_응답.getTripScheduleResponses().get(1).getScheduleName()).isEqualTo("일정3");
            assertThat(플래너_조회_응답.getTripScheduleResponses().get(2).getScheduleName()).isEqualTo("일정2");
            assertThat(플래너_조회_응답.getTripScheduleResponses().get(3).getScheduleName()).isEqualTo("일정1");
        });
    }

    @DisplayName("플래너 여행 일정을 주어진 범위 내에서 정렬시, 범위의 시작날짜가 종료날짜보다 이후라면 예외가 발생한다.")
    @Test
    void 플래너_여행_일정을_주어진_범위_내에서_정렬시_범위의_시작날짜가_종료날짜보다_이후라면_예외가_발생한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(여행_일정1_생성(하온)); tripScheduleRepository.save(여행_일정2_생성(하온));
        tripScheduleRepository.save(여행_일정3_생성(하온)); tripScheduleRepository.save(여행_일정4_생성(하온));

        LocalDate 종료날짜 = LocalDate.now().minusDays(1);
        LocalDate 시작날짜 = LocalDate.now().plusDays(1);

        FindPlannerOrderByDateBetweenRequest 플래너_조회_요청 = new FindPlannerOrderByDateBetweenRequest(시작날짜, 종료날짜);

        // when, then
        assertThatThrownBy(() -> plannerService.findPlannerOrderByDateAndRange(하온.getId(), 플래너_조회_요청))
                .isInstanceOf(InvalidDateSequenceException.class);
    }

    @DisplayName("공개 상태인 이번달의 모든 멤버에 대한 여행 일정을 찾는다.")
    @Test
    void 공개_상태인_이번달의_모든_멤버에_대한_여행_일정을_찾는다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(이번달_공개_여행_일정1_생성(하온)); tripScheduleRepository.save(이번달_공개_여행_일정2_생성(하온));
        tripScheduleRepository.save(이번달_공개_여행_일정3_생성(하온)); tripScheduleRepository.save(이번달_공개_여행_일정4_생성(하온));

        Member 리안 = memberRepository.save(리안_기존());
        tripScheduleRepository.save(이번달_공개_여행_일정1_생성(리안)); tripScheduleRepository.save(이번달_공개_여행_일정2_생성(리안));
        tripScheduleRepository.save(이번달_공개_여행_일정3_생성(리안)); tripScheduleRepository.save(이번달_공개_여행_일정4_생성(리안));

        // when
        List<TripScheduleResponse> actual = plannerService.findPublicSchedulesForCurrentMonth().getTripScheduleResponses();

        // then
        assertEquals(actual.size(), 8);
    }

    @DisplayName("이번달 여행 일정 조회시, 비공개 상태인 여행 일정은 검색 대상에서 제외된다.")
    @Test
    void 이번달_여행_일정_조회시_비공개_상태인_여행_일정은_검색_대상에서_제외된다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(이번달_공개_여행_일정1_생성(하온)); tripScheduleRepository.save(이번달_공개_여행_일정2_생성(하온));
        tripScheduleRepository.save(이번달_비공개_여행_일정5_생성(하온)); tripScheduleRepository.save(이번달_비공개_여행_일정6_생성(하온));

        Member 리안 = memberRepository.save(리안_기존());
        tripScheduleRepository.save(이번달_공개_여행_일정1_생성(리안)); tripScheduleRepository.save(이번달_공개_여행_일정2_생성(리안));
        tripScheduleRepository.save(이번달_비공개_여행_일정5_생성(리안)); tripScheduleRepository.save(이번달_비공개_여행_일정6_생성(리안));

        // when
        List<TripScheduleResponse> actual = plannerService.findPublicSchedulesForCurrentMonth().getTripScheduleResponses();

        // then
        assertEquals(actual.size(), 4);
    }

    @DisplayName("이번달에 해당하지 않는 여행 일정은 검색 대상에서 제외된다.")
    @Test
    void 이번달에_해당하지_않는_여행_일정은_검색_일정에서_제외된다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(이번달_공개_여행_일정1_생성(하온)); tripScheduleRepository.save(이번달_공개_여행_일정2_생성(하온));
        tripScheduleRepository.save(지난달_공개_여행_일정1_생성(하온)); tripScheduleRepository.save(지난달_공개_여행_일정2_생성(하온));

        Member 리안 = memberRepository.save(리안_기존());
        tripScheduleRepository.save(이번달_공개_여행_일정1_생성(리안)); tripScheduleRepository.save(이번달_공개_여행_일정2_생성(리안));
        tripScheduleRepository.save(지난달_공개_여행_일정1_생성(리안)); tripScheduleRepository.save(지난달_공개_여행_일정2_생성(리안));

        // when
        List<TripScheduleResponse> actual = plannerService.findPublicSchedulesForCurrentMonth().getTripScheduleResponses();

        // then
        assertEquals(actual.size(), 4);
    }

    @DisplayName("공개된 여행지를 주어진 범위 내에서 생성날짜 기준으로 내림차순 정렬한다.")
    @Test
    void 공개된_여행지를_주어진_범위_내에서_생성날짜_기준으로_내림차순_정렬한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(여행_일정1_생성(하온)); tripScheduleRepository.save(여행_일정2_생성(하온));
        tripScheduleRepository.save(여행_일정3_생성(하온)); tripScheduleRepository.save(여행_일정4_생성(하온));

        LocalDate 시작날짜 = LocalDate.now().minusDays(1);
        LocalDate 종료날짜 = LocalDate.now().plusDays(1);
        FindPublicSchedulesForRangeRequest 플래너_조회_요청 = new FindPublicSchedulesForRangeRequest(시작날짜, 종료날짜);
        Pageable pageable = PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "createdAt"));

        // when
        List<TripScheduleRangeResponse> actual = plannerService.findPublicSchedulesForCreatedAtRange(플래너_조회_요청, pageable).getTripScheduleResponses();

        assertAll(() -> {
            assertEquals(actual.size(), 4);
            assertEquals(actual.get(0).getScheduleName(), "일정4");
            assertEquals(actual.get(1).getScheduleName(), "일정3");
            assertEquals(actual.get(2).getScheduleName(), "일정2");
            assertEquals(actual.get(3).getScheduleName(), "일정1");
        });
    }

    @DisplayName("등록날짜 기반 여행 일정을 검색할 때, 비공개 상태인 여행 일정은 검색 대상에서 제외된다.")
    @Test
    void 등록날짜_기반_여행_일정을_검색할_때_비공개_상태인_여행_일정은_검색_대상에서_제외된다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(이번달_공개_여행_일정1_생성(하온)); tripScheduleRepository.save(이번달_공개_여행_일정2_생성(하온));
        tripScheduleRepository.save(이번달_비공개_여행_일정5_생성(하온)); tripScheduleRepository.save(이번달_비공개_여행_일정6_생성(하온));

        LocalDate 시작날짜 = LocalDate.now().minusDays(30);
        LocalDate 종료날짜 = LocalDate.now().plusDays(30);
        FindPublicSchedulesForRangeRequest 플래너_조회_요청 = new FindPublicSchedulesForRangeRequest(시작날짜, 종료날짜);
        Pageable pageable = PageRequest.of(0, 30, Sort.by(Sort.Direction.DESC, "createdAt"));

        // when
        FindPlannerPublicForCreatedAtRangeResponses 플래너_조회_응답 = plannerService.findPublicSchedulesForCreatedAtRange(플래너_조회_요청, pageable);

        // then
        assertEquals(플래너_조회_응답.getTripScheduleResponses().size(), 2);
    }

    @DisplayName("여행 일정 이름으로 여행 일정 리스트를 찾는다.")
    @Test
    void 여행_일정_이름으로_여행_일정_리스틀_찾는다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(여행_일정1_생성(하온)); tripScheduleRepository.save(여행_일정2_생성(하온));
        tripScheduleRepository.save(여행_일정3_생성(하온)); tripScheduleRepository.save(여행_일정4_생성(하온));

        FindSchedulesByNameRequest 검색_이름_요청 = new FindSchedulesByNameRequest("일정1");

        // when
        FindSchedulesNameResponses 여행_일정_검색_결과 = plannerService.findSchedulesByName(검색_이름_요청);

        // then
        assertEquals(여행_일정_검색_결과.getTripScheduleResponses().size(),1);
    }

    @DisplayName("비공개 여행 일정은 이름 검색 대상에서 제외된다.")
    @Test
    void 비공개_여행_일정은_이름_검색_대상에서_제외된다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(이번달_비공개_여행_일정5_생성(하온));
        FindSchedulesByNameRequest 검색_이름_요청 = new FindSchedulesByNameRequest("일정5");

        // when
        FindSchedulesNameResponses 여행_일정_검색_결과 = plannerService.findSchedulesByName(검색_이름_요청);

        // then
        assertEquals(여행_일정_검색_결과.getTripScheduleResponses().size(), 0);
    }
}
