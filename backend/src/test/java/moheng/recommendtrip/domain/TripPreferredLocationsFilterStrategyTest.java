package moheng.recommendtrip.domain;

import static moheng.config.fixture.MemberFixtures.하온_기존;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.ServiceTestConfig;
import moheng.liveinformation.domain.*;
import moheng.liveinformation.domain.repository.LiveInformationRepository;
import moheng.liveinformation.domain.repository.MemberLiveInformationRepository;
import moheng.liveinformation.domain.repository.TripLiveInformationRepository;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.recommendtrip.domain.filterinfo.PreferredLocationsFilterInfo;
import moheng.recommendtrip.domain.repository.RecommendTripRepository;
import moheng.recommendtrip.domain.tripfilterstrategy.TripPreferredLocationsFilterStrategy;
import moheng.trip.domain.MemberTrip;
import moheng.trip.domain.repository.MemberTripRepository;
import moheng.trip.domain.Trip;
import moheng.trip.domain.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

    @DisplayName("선호 여행지로 필터링된 여행지 리스트는 정확히 10개가 리턴된다.")
    @Test
    void 선호_여행지로_필터링된_여행지_리스트는_정확히_10개가_리턴된다() {
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

    @DisplayName("선호 여행지로 AI 맞춤 추천받은 여행지 리스트는 추가적으로 동일한 생활정보를 가진 여행지들로 필터링된다.")
    @Test
    void 선호_여행지로_AI_맞춤_추천받은_여행지_리스트는_추가적으로_동일한_생활정보를_가진_여행지들로_필터링된다() {
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
        Trip trip11 = tripRepository.save(new Trip("여행지10", "장소명3", 10L, "설명3", "이미지 경로3"));
        Trip trip12 = tripRepository.save(new Trip("여행지10", "장소명3", 10L, "설명3", "이미지 경로3"));
        Trip trip13 = tripRepository.save(new Trip("멤버가 관심없는 생활정보에 대한 여행지", "장소명3", 11L, "설명3", "이미지 경로3"));
        Trip trip14 = tripRepository.save(new Trip("멤버가 관심없는 생활정보에 대한 여행지", "장소명3", 12L, "설명3", "이미지 경로3"));
        memberTripRepository.save(new MemberTrip(member, trip1, 10L)); memberTripRepository.save(new MemberTrip(member, trip2, 20L));
        memberTripRepository.save(new MemberTrip(member, trip3, 30L)); memberTripRepository.save(new MemberTrip(member, trip4, 30L));
        memberTripRepository.save(new MemberTrip(member, trip5, 30L)); memberTripRepository.save(new MemberTrip(member, trip6, 30L));
        memberTripRepository.save(new MemberTrip(member, trip7, 30L)); memberTripRepository.save(new MemberTrip(member, trip8, 30L));
        memberTripRepository.save(new MemberTrip(member, trip9, 30L)); memberTripRepository.save(new MemberTrip(member, trip10, 30L));
        memberTripRepository.save(new MemberTrip(member, trip13, 30L));
        memberTripRepository.save(new MemberTrip(member, trip14, 30L));
        recommendTripRepository.save(new RecommendTrip(trip1, member, 1L)); recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 3L)); recommendTripRepository.save(new RecommendTrip(trip4, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 5L)); recommendTripRepository.save(new RecommendTrip(trip6, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 7L)); recommendTripRepository.save(new RecommendTrip(trip8, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip13, member, 9L)); recommendTripRepository.save(new RecommendTrip(trip14, member, 10L));
        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        LiveInformation otherLiveInformation = liveInformationRepository.save(new LiveInformation("멤버가 관심없는 다른 생활정보"));
        memberLiveInformationRepository.save(new MemberLiveInformation(liveInformation, member));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip1)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip11)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip12));
        tripLiveInformationRepository.save(new TripLiveInformation(otherLiveInformation, trip13));
        tripLiveInformationRepository.save(new TripLiveInformation(otherLiveInformation, trip14));

        // when
        List<Trip> trips = tripPreferredLocationsFilterStrategy.execute(new PreferredLocationsFilterInfo(member.getId()));

        // then
        List<LiveInformation> liveInformations = tripLiveInformationRepository.findLiveInformationByTrips(trips);

        assertAll(() -> {
            assertThat(liveInformations).isNotEmpty();
            assertThat(liveInformations)
                    .allMatch(liveInfo -> "생활정보1".equals(liveInfo.getName()));
        });
    }

    @DisplayName("멤버의 생활정보가 없으면 추가적인 생활정보 기반 필터링이 수행되지 않는다.")
    @Test
    void 멤버의_생활정보가_없으면_추가적인_생활정보_기반_필터링이_수행되지_않는다() {
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
        LiveInformation liveInformation1 = liveInformationRepository.save(new LiveInformation("생활정보1"));
        LiveInformation liveInformation2 = liveInformationRepository.save(new LiveInformation("생활정보2"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation1, trip1)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation1, trip2));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation1, trip3)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation1, trip4));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation1, trip5)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation1, trip6));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation1, trip7)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation1, trip8));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation2, trip9)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation2, trip10));

        // when
        List<Trip> trips = tripPreferredLocationsFilterStrategy.execute(new PreferredLocationsFilterInfo(member.getId()));

        // then
        List<LiveInformation> liveInformations = tripLiveInformationRepository.findLiveInformationByTrips(trips);

        assertThat(trips).hasSize(10);
        assertThat(liveInformations).hasSize(2);
    }
}
