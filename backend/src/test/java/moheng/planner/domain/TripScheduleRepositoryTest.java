package moheng.planner.domain;

import static moheng.fixture.TripFixture.*;
import static moheng.fixture.TripScheduleFixtures.*;
import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.RepositoryTestConfig;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.planner.domain.repository.TripScheduleRegistryRepository;
import moheng.planner.domain.repository.TripScheduleRepository;
import moheng.trip.domain.Trip;
import moheng.trip.domain.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TripScheduleRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private TripScheduleRepository tripScheduleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TripScheduleRegistryRepository tripScheduleRegistryRepository;

    @DisplayName("여행 일정을 생성날짜 기준으로 내림차순 정렬한다.")
    @Test
    void 여행_일정을_생성날짜_기준으로_내림차순_정렬한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(여행_일정2_생성(하온)); tripScheduleRepository.save(여행_일정3_생성(하온)); tripScheduleRepository.save(여행_일정1_생성(하온));

        // when
        List<TripSchedule> actual = tripScheduleRepository.findByMemberOrderByCreatedAtDesc(하온);

        // then
        assertAll(() -> {
            assertThat(actual.get(0).getName()).isEqualTo("일정1");
            assertThat(actual.get(1).getName()).isEqualTo("일정3");
            assertThat(actual.get(2).getName()).isEqualTo("일정2");
        });
    }

    @DisplayName("여행 일정을 시작날짜 기준으로 오름차순 정렬한다.")
    @Test
    void 여행_일정을_시작날짜_기준으로_오름차순_정렬한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(여행_일정1_생성(하온)); tripScheduleRepository.save(여행_일정2_생성(하온)); tripScheduleRepository.save(여행_일정3_생성(하온));

        // when
        List<TripSchedule> actual = tripScheduleRepository.findByMemberOrderByStartDateAsc(하온);

        // then
        assertAll(() -> {
            assertThat(actual.get(0).getName()).isEqualTo("일정1");
            assertThat(actual.get(1).getName()).isEqualTo("일정2");
            assertThat(actual.get(2).getName()).isEqualTo("일정3");
        });
    }

    @DisplayName("여행 일정을 이름순 기준으로 오름차순 정렬한다.")
    @Test
    void 여행_일정을_이름순_기준으로_오름차순_정렬한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(여행_일정_가_생성(하온)); tripScheduleRepository.save(여행_일정_다_생성(하온)); tripScheduleRepository.save(여행_일정_나_생성(하온));

        // when
        List<TripSchedule> actual = tripScheduleRepository.findByMemberOrderByNameAsc(하온);

        // then
        assertAll(() -> {
            assertThat(actual.get(0).getName()).isEqualTo("가 일정");
            assertThat(actual.get(1).getName()).isEqualTo("나 일정");
            assertThat(actual.get(2).getName()).isEqualTo("다 일정");
        });
    }

    @DisplayName("멤버의 여행 일정중에 이미 일정이 존재하면 참을 리턴한다.")
    @Test
    void 맴버의_여행_일정중에_이미_일정이_존재하면_참을_리턴한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(여행_일정_가_생성(하온));

        // when, then
        assertTrue(tripScheduleRepository.existsByMemberAndName(하온, "가 일정"));
    }

    @DisplayName("일정에 담겨있는 여행지 리스트를 찾는다.")
    @Test
    void 일정에_담겨있는_여행지_리스트를_찾는다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        Trip 여행지1 = tripRepository.save(여행지1_생성()); Trip 여행지2 = tripRepository.save(여행지2_생성());

        TripSchedule 여행_일정1 = tripScheduleRepository.save(여행_일정1_생성(하온));

        tripScheduleRegistryRepository.save(new TripScheduleRegistry(여행지1, 여행_일정1));
        tripScheduleRegistryRepository.save(new TripScheduleRegistry(여행지2, 여행_일정1));

        // when
        List<Trip> trips = tripRepository.findTripsByScheduleId(여행_일정1.getId());

        // then
        assertThat(trips.size()).isEqualTo(2);
    }

    @DisplayName("플래너 여행 일정을 주어진 범위 내에서 생성날짜를 기준으로 내림차순 정렬한다.")
    @Test
    void 플래너_여행_일정을_주어진_범위_내에서_생성날짜를_기준으로_내림차순_정렬한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        tripScheduleRepository.save(여행_일정1_생성(하온)); tripScheduleRepository.save(여행_일정2_생성(하온));
        tripScheduleRepository.save(여행_일정3_생성(하온)); tripScheduleRepository.save(여행_일정4_생성(하온));

        LocalDateTime 시작날짜 = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime 종료날짜 = LocalDate.now().plusDays(1).atStartOfDay();

        // when
        List<TripSchedule> 플래너_일정_조회_결과 = tripScheduleRepository.findByMemberAndDateRangeOrderByCreatedAt(하온, 시작날짜, 종료날짜);

        // then
        assertAll(() -> {
            assertEquals(플래너_일정_조회_결과.size(), 4);
            assertThat(플래너_일정_조회_결과.get(0).getName()).isEqualTo("일정4");
            assertThat(플래너_일정_조회_결과.get(1).getName()).isEqualTo("일정3");
            assertThat(플래너_일정_조회_결과.get(2).getName()).isEqualTo("일정2");
            assertThat(플래너_일정_조회_결과.get(3).getName()).isEqualTo("일정1");
        });
    }
}
