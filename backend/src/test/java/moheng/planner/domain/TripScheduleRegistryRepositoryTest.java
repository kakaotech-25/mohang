package moheng.planner.domain;

import static moheng.fixture.TripFixture.*;
import static moheng.fixture.TripScheduleFixtures.*;
import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

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

public class TripScheduleRegistryRepositoryTest extends RepositoryTestConfig {
    @Autowired
    private TripScheduleRegistryRepository tripScheduleRegistryRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TripScheduleRepository tripScheduleRepository;

    @DisplayName("세부 일정의 모든 여행지를 제거한다.")
    @Test
    void 세부_여행지의_모든_여행지를_제거한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        TripSchedule 여행_일정1 = tripScheduleRepository.save(여행_일정1_생성(하온));
        Trip 여행지1 = tripRepository.save(여행지1_생성()); Trip 여행지2 = tripRepository.save(여행지2_생성()); Trip 여행지3 = tripRepository.save(여행지3_생성());
        tripScheduleRegistryRepository.save(여행_일정_등록_생성(여행지1, 여행_일정1));
        tripScheduleRegistryRepository.save(여행_일정_등록_생성(여행지2, 여행_일정1));
        tripScheduleRegistryRepository.save(여행_일정_등록_생성(여행지3, 여행_일정1));

        // when
        tripScheduleRegistryRepository.deleteAllByTripScheduleId(여행_일정1.getId());

        // then
        long exptected = 0L;
        assertThat(tripScheduleRegistryRepository.count()).isEqualTo(exptected);
    }

    @DisplayName("세부 일정내의 특정 여행지를 제거한다.")
    @Test
    void 세부_일정내의_특정_여행지를_제거한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        TripSchedule 여행_일정1 = tripScheduleRepository.save(여행_일정1_생성(하온));
        Trip 여행지1 = tripRepository.save(여행지1_생성()); Trip 여행지2 = tripRepository.save(여행지2_생성()); Trip 여행지3 = tripRepository.save(여행지3_생성());
        tripScheduleRegistryRepository.save(new TripScheduleRegistry(여행지1, 여행_일정1));
        tripScheduleRegistryRepository.save(new TripScheduleRegistry(여행지2, 여행_일정1));
        tripScheduleRegistryRepository.save(new TripScheduleRegistry(여행지3, 여행_일정1));

        // when
        tripScheduleRegistryRepository.deleteByTripIdAndTripScheduleId(여행지1.getId(), 여행_일정1.getId());

        // then
        long exptected = 2L;
        assertThat(tripScheduleRegistryRepository.count()).isEqualTo(exptected);
    }
}
