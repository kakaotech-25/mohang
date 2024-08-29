package moheng.recommendtrip.domain;

import static moheng.fixture.MemberFixtures.하온_기존;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.ServiceTestConfig;
import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.TripKeyword;
import moheng.liveinformation.domain.*;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.recommendtrip.domain.filterinfo.PreferredLocationsFilterInfo;
import moheng.recommendtrip.domain.tripfilterstrategy.TripPreferredLocationsFilterStrategy;
import moheng.trip.domain.MemberTrip;
import moheng.trip.domain.MemberTripRepository;
import moheng.trip.domain.Trip;
import moheng.trip.domain.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TripPreferredLocationsFilterStrategyTest extends ServiceTestConfig {
    @Autowired
    private TripPreferredLocationsFilterStrategy tripPreferredLocationsFilterStrategy;

    @Autowired
    private RecommendTripRepository recommendTripRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberTripRepository memberTripRepository;

    @Autowired
    private LiveInformationRepository liveInformationRepository;

    @Autowired
    private MemberLiveInformationRepository memberLiveInformationRepository;

    @Autowired
    private TripLiveInformationRepository tripLiveInformationRepository;

    @DisplayName("PREFERRED_LOCATIONS 전략 네이밍에 일치하면 전략이 수행된다.")
    @Test
    void PREFERRED_LOCATIONS_전략_네이밍에_일치하면_전략이_수행된다() {
        // given
        String PREFERRED_LOCATIONS = "PREFERRED_LOCATIONS";

        // when, then
        assertTrue(() -> tripPreferredLocationsFilterStrategy.isMatch(PREFERRED_LOCATIONS));
    }

    @DisplayName("선호 여행지 기반 필터링을 수행한다.")
    @Test
    void 선호_여행지_기반_필터링을_수행한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip trip1 = tripRepository.save(new Trip("여행지1", "장소명1", 1L, "설명1", "이미지 경로1"));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "장소명2", 2L, "설명2", "이미지 경로2"));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "장소명3", 3L, "설명3", "이미지 경로3"));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "장소명3", 4L, "설명3", "이미지 경로3"));
        Trip trip5 = tripRepository.save(new Trip("여행지5", "장소명3", 5L, "설명3", "이미지 경로3"));
        Trip trip6 = tripRepository.save(new Trip("여행지6", "장소명3", 6L, "설명3", "이미지 경로3"));
        Trip trip7 = tripRepository.save(new Trip("여행지7", "장소명3", 7L, "설명3", "이미지 경로3"));
        Trip trip8 = tripRepository.save(new Trip("여행지8", "장소명3", 8L, "설명3", "이미지 경로3"));
        Trip trip9 = tripRepository.save(new Trip("여행지9", "장소명3", 9L, "설명3", "이미지 경로3"));
        Trip trip10 = tripRepository.save(new Trip("여행지10", "장소명3", 10L, "설명3", "이미지 경로3"));
        memberTripRepository.save(new MemberTrip(member, trip1, 10L)); memberTripRepository.save(new MemberTrip(member, trip2, 20L));
        memberTripRepository.save(new MemberTrip(member, trip3, 30L)); memberTripRepository.save(new MemberTrip(member, trip4, 30L));
        memberTripRepository.save(new MemberTrip(member, trip5, 30L)); memberTripRepository.save(new MemberTrip(member, trip6, 30L));
        memberTripRepository.save(new MemberTrip(member, trip7, 30L)); memberTripRepository.save(new MemberTrip(member, trip8, 30L));
        memberTripRepository.save(new MemberTrip(member, trip9, 30L)); memberTripRepository.save(new MemberTrip(member, trip10, 30L));
        recommendTripRepository.save(new RecommendTrip(trip1, member, 1L)); recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 3L)); recommendTripRepository.save(new RecommendTrip(trip4, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 5L)); recommendTripRepository.save(new RecommendTrip(trip6, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 7L)); recommendTripRepository.save(new RecommendTrip(trip8, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 9L)); recommendTripRepository.save(new RecommendTrip(trip10, member, 10L));
        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        memberLiveInformationRepository.save(new MemberLiveInformation(liveInformation, member));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip1)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10));

        // when, then
        assertThat(tripPreferredLocationsFilterStrategy.execute(new PreferredLocationsFilterInfo(member.getId()))).hasSize(10);
    }
}
