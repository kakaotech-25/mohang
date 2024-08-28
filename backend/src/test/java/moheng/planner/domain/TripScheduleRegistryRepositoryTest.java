package moheng.planner.domain;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.RepositoryTestConfig;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.trip.domain.Trip;
import moheng.trip.domain.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

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
        Member member = memberRepository.save(하온_기존());
        TripSchedule tripSchedule = tripScheduleRepository.save(new TripSchedule("멤버 일정", LocalDate.of(2020, 8, 1), LocalDate.of(2024, 8, 2), member));
        Trip trip1 = tripRepository.save(new Trip("여행지1", "장소명", 1L, "설명", "https://image.com", 126.3307690830, 36.5309210243));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "장소명", 2L, "설명", "https://image.com", 226.3307690830, 46.5309210243));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "장소명", 3L, "설명", "https://image.com", 326.3307690830, 56.5309210243));
        tripScheduleRegistryRepository.save(new TripScheduleRegistry(trip1, tripSchedule));
        tripScheduleRegistryRepository.save(new TripScheduleRegistry(trip2, tripSchedule));
        tripScheduleRegistryRepository.save(new TripScheduleRegistry(trip3, tripSchedule));

        // when
        tripScheduleRegistryRepository.deleteAllByTripScheduleId(tripSchedule.getId());

        // then
        long exptected = 0L;
        assertThat(tripScheduleRegistryRepository.count()).isEqualTo(exptected);
    }

    @DisplayName("세부 일정내의 특정 여행지를 제거한다.")
    @Test
    void 세부_일정내의_특정_여행지를_제거한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        TripSchedule tripSchedule = tripScheduleRepository.save(new TripSchedule("멤버 일정", LocalDate.of(2020, 8, 1), LocalDate.of(2024, 8, 2), member));
        Trip trip1 = tripRepository.save(new Trip("여행지1", "장소명", 1L, "설명", "https://image.com", 126.3307690830, 36.5309210243));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "장소명", 2L, "설명", "https://image.com", 226.3307690830, 46.5309210243));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "장소명", 3L, "설명", "https://image.com", 326.3307690830, 56.5309210243));
        tripScheduleRegistryRepository.save(new TripScheduleRegistry(trip1, tripSchedule));
        tripScheduleRegistryRepository.save(new TripScheduleRegistry(trip2, tripSchedule));
        tripScheduleRegistryRepository.save(new TripScheduleRegistry(trip3, tripSchedule));

        // when
        tripScheduleRegistryRepository.deleteByTripIdAndTripScheduleId(trip1.getId(), tripSchedule.getId());

        // then
        long exptected = 2L;
        assertThat(tripScheduleRegistryRepository.count()).isEqualTo(exptected);
    }
}
