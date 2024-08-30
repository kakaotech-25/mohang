package moheng.recommendtrip.domain;

import static moheng.fixture.MemberFixtures.하온_기존;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.ServiceTestConfig;
import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.LiveInformationRepository;
import moheng.liveinformation.domain.TripLiveInformation;
import moheng.liveinformation.domain.TripLiveInformationRepository;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.recommendtrip.domain.filterinfo.FilterStandardInfo;
import moheng.recommendtrip.domain.filterinfo.LiveInformationFilterInfo;
import moheng.recommendtrip.domain.tripfilterstrategy.TripLiveInformationFilterStrategy;
import moheng.trip.domain.Trip;
import moheng.trip.domain.TripRepository;
import moheng.trip.dto.FindTripWithSimilarTripsResponse;
import moheng.trip.exception.NoExistTripException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TripLiveInformationStrategyTest extends ServiceTestConfig {
    @Autowired
    private TripLiveInformationFilterStrategy tripLiveInformationFilterStrategy;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private RecommendTripRepository recommendTripRepository;

    @Autowired
    private TripLiveInformationRepository tripLiveInformationRepository;

    @Autowired
    private LiveInformationRepository liveInformationRepository;

    @DisplayName("LIVE_INFO 전략 네이밍과 일치하면 전략이 수행된다.")
    @Test
    void LIVE_INFO_전략_네이밍과_일치하면_전략이_수행된다() {
        // given
        String LIVE_INFO = "LIVE_INFO";

        // when, then
        assertTrue(() -> tripLiveInformationFilterStrategy.isMatch(LIVE_INFO));
    }

    @DisplayName("존재하지 않는 여행지에 기반하여 생활정보를 필터링하려고 하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_여행지에_기반하여_생활정보를_필터링하려고_하면_예외가_발생한다() {
        // given
        long invalidTripId = -1L;
        FilterStandardInfo filterStandardInfo = new LiveInformationFilterInfo(invalidTripId);

        // when, then
        assertThatThrownBy(() -> tripLiveInformationFilterStrategy.execute(filterStandardInfo))
                .isInstanceOf(NoExistTripException.class);
    }

    @DisplayName("생활정보로 필터링된 여행지 리스트는 정확히 10개가 리턴된다.")
    @Test
    void 생활정보로_필터링된_여행지_리스트는_정확히_10개가_리턴된다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip currentTrip = tripRepository.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip5 = tripRepository.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        Trip trip6 = tripRepository.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        Trip trip7 = tripRepository.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        Trip trip8 = tripRepository.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        Trip trip9 = tripRepository.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        Trip trip10 = tripRepository.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip11 = tripRepository.save(new Trip("여행지11", "서울", 11L, "설명11", "https://image.png", 0L));
        Trip trip12 = tripRepository.save(new Trip("여행지12", "서울", 12L, "설명12", "https://image.png", 0L));
        Trip trip13 = tripRepository.save(new Trip("여행지13", "서울", 13L, "설명12", "https://image.png", 0L));
        recommendTripRepository.save(new RecommendTrip(currentTrip, member, 1L));
        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, currentTrip));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip11));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip12)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip13));

        recommendTripRepository.save(new RecommendTrip(trip2, member, 1L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip4, member, 3L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip6, member, 5L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip8, member, 7L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip10, member, 9L));
        recommendTripRepository.save(new RecommendTrip(trip11, member, 10L));

        // when
        FilterStandardInfo filterStandardInfo = new LiveInformationFilterInfo(currentTrip.getId());
        List<Trip> actual = tripLiveInformationFilterStrategy.execute(filterStandardInfo);

        // then
        assertThat(actual).hasSize(10);
    }

    @DisplayName("현재 여행지와 동일한 생활정보를 가진 여행지들로만 필터링한다.")
    @Test
    void 현재_여행지와_동일한_생활정보를_가진_여행지들로만_필터링한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip currentTrip = tripRepository.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip5 = tripRepository.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        Trip trip6 = tripRepository.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        Trip trip7 = tripRepository.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        Trip trip8 = tripRepository.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        Trip trip9 = tripRepository.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        Trip trip10 = tripRepository.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip11 = tripRepository.save(new Trip("여행지11", "서울", 11L, "설명11", "https://image.png", 0L));
        Trip otherTrip12 = tripRepository.save(new Trip("여행지12", "서울", 12L, "설명12", "https://image.png", 0L));
        Trip otherTrip13 = tripRepository.save(new Trip("여행지13", "서울", 13L, "설명12", "https://image.png", 0L));
        recommendTripRepository.save(new RecommendTrip(currentTrip, member, 1L));
        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("현재 생활정보"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, currentTrip));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip11));

        LiveInformation otherLiveInformation = liveInformationRepository.save(new LiveInformation("다른 생활정보"));
        tripLiveInformationRepository.save(new TripLiveInformation(otherLiveInformation, otherTrip12)); tripLiveInformationRepository.save(new TripLiveInformation(otherLiveInformation, otherTrip13));

        recommendTripRepository.save(new RecommendTrip(trip2, member, 1L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip4, member, 3L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip6, member, 5L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip8, member, 7L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 8L));
        recommendTripRepository.save(new RecommendTrip(otherTrip12, member, 10L));
        recommendTripRepository.save(new RecommendTrip(otherTrip13, member, 9L));

        // when
        FilterStandardInfo filterStandardInfo = new LiveInformationFilterInfo(currentTrip.getId());
        List<Trip> actual = tripLiveInformationFilterStrategy.execute(filterStandardInfo);

        // then
        assertThat(actual).hasSize(10);
    }
}
