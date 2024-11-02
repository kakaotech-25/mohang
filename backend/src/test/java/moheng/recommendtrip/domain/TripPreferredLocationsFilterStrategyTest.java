package moheng.recommendtrip.domain;

import static moheng.fixture.KeywordFixture.*;
import static moheng.fixture.KeywordFixture.키워드10_생성;
import static moheng.fixture.LiveInformationFixture.*;
import static moheng.fixture.MemberFixtures.하온_기존;
import static moheng.fixture.MemberTripFixture.*;
import static moheng.fixture.MemberTripFixture.멤버의_여행지_방문수_30_생성;
import static moheng.fixture.RecommendTripFixture.*;
import static moheng.fixture.RecommendTripFixture.선호_여행지_생성_랭크10;
import static moheng.fixture.TripFixture.*;
import static moheng.fixture.TripFixture.여행지10_생성;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.ServiceTestConfig;
import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.TripKeyword;
import moheng.keyword.domain.repository.KeywordRepository;
import moheng.keyword.domain.repository.TripKeywordRepository;
import moheng.liveinformation.domain.*;
import moheng.liveinformation.domain.repository.LiveInformationRepository;
import moheng.liveinformation.domain.repository.MemberLiveInformationRepository;
import moheng.liveinformation.domain.repository.TripLiveInformationRepository;
import moheng.member.application.MemberService;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.dto.request.SignUpInterestTripsRequest;
import moheng.member.exception.NoExistMemberException;
import moheng.recommendtrip.domain.filterinfo.PreferredLocationsFilterInfo;
import moheng.recommendtrip.domain.repository.RecommendTripRepository;
import moheng.recommendtrip.domain.tripfilterstrategy.TripPreferredLocationsFilterStrategy;
import moheng.recommendtrip.exception.LackOfRecommendTripException;
import moheng.trip.domain.MemberTrip;
import moheng.trip.domain.repository.MemberTripRepository;
import moheng.trip.domain.Trip;
import moheng.trip.domain.repository.TripRepository;
import moheng.trip.dto.FindTripsResponse;
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

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private TripKeywordRepository tripKeywordRepository;

    @Autowired
    private MemberService memberService;

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
        Member 하온 = memberRepository.save(하온_기존());
        Trip 여행지1 = tripRepository.save(여행지1_생성()); Trip 여행지2 = tripRepository.save(여행지2_생성());
        Trip 여행지3 = tripRepository.save(여행지3_생성()); Trip 여행지4 = tripRepository.save(여행지4_생성());
        Trip 여행지5 = tripRepository.save(여행지5_생성()); Trip 여행지6 = tripRepository.save(여행지6_생성());
        Trip 여행지7 = tripRepository.save(여행지7_생성()); Trip 여행지8 = tripRepository.save(여행지8_생성());
        Trip 여행지9 = tripRepository.save(여행지9_생성()); Trip 여행지10 = tripRepository.save(여행지10_생성());
        memberTripRepository.save(멤버의_여행지_방문수_10_생성(하온, 여행지1)); memberTripRepository.save(멤버의_여행지_방문수_20_생성(하온, 여행지2));
        memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지3)); memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지4));
        memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지5)); memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지6));
        memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지7)); memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지8));
        memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지9)); memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지10));
        recommendTripRepository.save(선호_여행지_생성_랭크1(여행지1, 하온)); recommendTripRepository.save(선호_여행지_생성_랭크2(여행지2, 하온));
        recommendTripRepository.save(선호_여행지_생성_랭크3(여행지3, 하온)); recommendTripRepository.save(선호_여행지_생성_랭크4(여행지4, 하온));
        recommendTripRepository.save(선호_여행지_생성_랭크5(여행지5, 하온)); recommendTripRepository.save(선호_여행지_생성_랭크6(여행지6, 하온));
        recommendTripRepository.save(선호_여행지_생성_랭크7(여행지7, 하온)); recommendTripRepository.save(선호_여행지_생성_랭크8(여행지8, 하온));
        recommendTripRepository.save(선호_여행지_생성_랭크9(여행지9, 하온)); recommendTripRepository.save(선호_여행지_생성_랭크10(여행지10, 하온));
        LiveInformation 생활정보1 = liveInformationRepository.save(생활정보1_생성());
        memberLiveInformationRepository.save(new MemberLiveInformation(생활정보1, 하온));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지1)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지2));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지3)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지4));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지5)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지6));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지7)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지8));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지9)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지10));

        // when, then
        assertThat(tripPreferredLocationsFilterStrategy.execute(new PreferredLocationsFilterInfo(하온.getId()))).hasSize(10);
    }

    @DisplayName("선호 여행지로 AI 맞춤 추천받은 여행지 리스트는 추가적으로 동일한 생활정보를 가진 여행지들로 필터링된다.")
    @Test
    void 선호_여행지로_AI_맞춤_추천받은_여행지_리스트는_추가적으로_동일한_생활정보를_가진_여행지들로_필터링된다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        Trip 여행지1 = tripRepository.save(여행지1_생성()); Trip 여행지2 = tripRepository.save(여행지2_생성());
        Trip 여행지3 = tripRepository.save(여행지3_생성()); Trip 여행지4 = tripRepository.save(여행지4_생성());
        Trip 여행지5 = tripRepository.save(여행지5_생성()); Trip 여행지6 = tripRepository.save(여행지6_생성());
        Trip 여행지7 = tripRepository.save(여행지7_생성()); Trip 여행지8 = tripRepository.save(여행지8_생성());
        Trip 여행지9 = tripRepository.save(여행지9_생성()); Trip 여행지10 = tripRepository.save(여행지10_생성());
        Trip 다른_여행지1 = tripRepository.save(new Trip("멤버가 관심없는 생활정보에 대한 여행지", "장소명3", 11L, "설명3", "이미지 경로3"));
        Trip 다른_여행지2 = tripRepository.save(new Trip("멤버가 관심없는 생활정보에 대한 여행지", "장소명3", 12L, "설명3", "이미지 경로3"));
        memberTripRepository.save(멤버의_여행지_방문수_10_생성(하온, 여행지1)); memberTripRepository.save(멤버의_여행지_방문수_20_생성(하온, 여행지2));
        memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지3)); memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지4));
        memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지5)); memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지6));
        memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지7)); memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지8));
        memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지9)); memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지10));
        memberTripRepository.save(new MemberTrip(하온, 다른_여행지1, 30L));
        memberTripRepository.save(new MemberTrip(하온, 다른_여행지2, 30L));
        recommendTripRepository.save(선호_여행지_생성_랭크1(여행지1, 하온)); recommendTripRepository.save(선호_여행지_생성_랭크2(여행지2, 하온));
        recommendTripRepository.save(선호_여행지_생성_랭크3(여행지3, 하온)); recommendTripRepository.save(선호_여행지_생성_랭크4(여행지4, 하온));
        recommendTripRepository.save(선호_여행지_생성_랭크5(여행지5, 하온)); recommendTripRepository.save(선호_여행지_생성_랭크6(여행지6, 하온));
        recommendTripRepository.save(선호_여행지_생성_랭크7(여행지7, 하온)); recommendTripRepository.save(선호_여행지_생성_랭크8(여행지8, 하온));
        recommendTripRepository.save(선호_여행지_생성_랭크9(여행지9, 하온)); recommendTripRepository.save(선호_여행지_생성_랭크10(여행지10, 하온));
        recommendTripRepository.save(new RecommendTrip(다른_여행지1, 하온, 9L)); recommendTripRepository.save(new RecommendTrip(다른_여행지2, 하온, 10L));
        LiveInformation 생활정보1 = liveInformationRepository.save(생활정보1_생성());
        LiveInformation 다른_생활정보 = liveInformationRepository.save(new LiveInformation("멤버가 관심없는 다른 생활정보"));
        memberLiveInformationRepository.save(new MemberLiveInformation(생활정보1, 하온));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지1)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지2));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지3)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지4));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지5)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지6));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지7)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지8));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지9)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지10));
        tripLiveInformationRepository.save(new TripLiveInformation(다른_생활정보, 다른_여행지1));
        tripLiveInformationRepository.save(new TripLiveInformation(다른_생활정보, 다른_여행지2));

        // when
        List<Trip> trips = tripPreferredLocationsFilterStrategy.execute(new PreferredLocationsFilterInfo(하온.getId()));

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
        Member 하온 = memberRepository.save(하온_기존());
        Trip 여행지1 = tripRepository.save(여행지1_생성()); Trip 여행지2 = tripRepository.save(여행지2_생성());
        Trip 여행지3 = tripRepository.save(여행지3_생성()); Trip 여행지4 = tripRepository.save(여행지4_생성());
        Trip 여행지5 = tripRepository.save(여행지5_생성()); Trip 여행지6 = tripRepository.save(여행지6_생성());
        Trip 여행지7 = tripRepository.save(여행지7_생성()); Trip 여행지8 = tripRepository.save(여행지8_생성());
        Trip 여행지9 = tripRepository.save(여행지9_생성()); Trip 여행지10 = tripRepository.save(여행지10_생성());
        memberTripRepository.save(멤버의_여행지_방문수_10_생성(하온, 여행지1)); memberTripRepository.save(멤버의_여행지_방문수_20_생성(하온, 여행지2));
        memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지3)); memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지4));
        memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지5)); memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지6));
        memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지7)); memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지8));
        memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지9)); memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지10));
        recommendTripRepository.save(선호_여행지_생성_랭크1(여행지1, 하온)); recommendTripRepository.save(선호_여행지_생성_랭크2(여행지2, 하온));
        recommendTripRepository.save(선호_여행지_생성_랭크3(여행지3, 하온)); recommendTripRepository.save(선호_여행지_생성_랭크4(여행지4, 하온));
        recommendTripRepository.save(선호_여행지_생성_랭크5(여행지5, 하온)); recommendTripRepository.save(선호_여행지_생성_랭크6(여행지6, 하온));
        recommendTripRepository.save(선호_여행지_생성_랭크7(여행지7, 하온)); recommendTripRepository.save(선호_여행지_생성_랭크8(여행지8, 하온));
        recommendTripRepository.save(선호_여행지_생성_랭크9(여행지9, 하온)); recommendTripRepository.save(선호_여행지_생성_랭크10(여행지10, 하온));
        LiveInformation 생활정보1 = liveInformationRepository.save(생활정보1_생성());
        LiveInformation 생활정보2 = liveInformationRepository.save(생활정보2_생성());
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지1)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지2));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지3)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지4));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지5)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지6));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지7)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지8));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보2, 여행지9)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보2, 여행지10));

        // when
        List<Trip> trips = tripPreferredLocationsFilterStrategy.execute(new PreferredLocationsFilterInfo(하온.getId()));

        // then
        List<LiveInformation> liveInformations = tripLiveInformationRepository.findLiveInformationByTrips(trips);

        assertThat(trips).hasSize(10);
        assertThat(liveInformations).hasSize(2);
    }

    @DisplayName("존재하지 않는 멤버기 AI 맞춤 10개의 여행지를 추천받으면 예외가 발생한다.")
    @Test
    void 존재하지_않는_멤버가_AI_맞춤_여행지를_추천받으면_예외가_발생한다() {
        // given
        long 존재하지_않는_멤버_ID = -1L;

        //when, then
        assertThatThrownBy(() -> tripPreferredLocationsFilterStrategy.execute(new PreferredLocationsFilterInfo(존재하지_않는_멤버_ID)))
                .isInstanceOf(NoExistMemberException.class);
    }

    @DisplayName("AI 맞춤 여행지를 추천시 멤버의 선호 여행지 정보가 5개 이하로 부족하면 예외가 발생한다.")
    @Test
    void AI_맞춤_여행지룰_추천시_맴버의_선호_여행지_정보가_5개_이하로_부족하면_예외가_발생한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        Trip 여행지1 = tripRepository.save(여행지1_생성()); Trip 여행지2 = tripRepository.save(여행지2_생성());
        Trip 여행지3 = tripRepository.save(여행지3_생성()); Trip 여행지4 = tripRepository.save(여행지4_생성());
        Trip 여행지5 = tripRepository.save(여행지5_생성()); Trip 여행지6 = tripRepository.save(여행지6_생성());
        Trip 여행지7 = tripRepository.save(여행지7_생성()); Trip 여행지8 = tripRepository.save(여행지8_생성());
        Trip 여행지9 = tripRepository.save(여행지9_생성()); Trip 여행지10 = tripRepository.save(여행지10_생성());
        Trip 다른_여행지1 = tripRepository.save(new Trip("멤버가 관심없는 생활정보에 대한 여행지", "장소명3", 11L, "설명3", "이미지 경로3"));
        Trip 다른_여행지2 = tripRepository.save(new Trip("멤버가 관심없는 생활정보에 대한 여행지", "장소명3", 12L, "설명3", "이미지 경로3"));
        memberTripRepository.save(멤버의_여행지_방문수_10_생성(하온, 여행지1)); memberTripRepository.save(멤버의_여행지_방문수_20_생성(하온, 여행지2));
        memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지3)); memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지4));
        memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지5)); memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지6));
        memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지7)); memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지8));
        memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지9)); memberTripRepository.save(멤버의_여행지_방문수_30_생성(하온, 여행지10));
        memberTripRepository.save(new MemberTrip(하온, 다른_여행지1, 30L));
        memberTripRepository.save(new MemberTrip(하온, 다른_여행지2, 30L));
        Keyword 키워드1 = keywordRepository.save(키워드1_생성()); Keyword 키워드2 = keywordRepository.save(키워드2_생성());
        Keyword 키워드3 = keywordRepository.save(키워드3_생성()); Keyword 키워드4 = keywordRepository.save(키워드4_생성());
        Keyword 키워드5 = keywordRepository.save(키워드5_생성()); Keyword 키워드6 = keywordRepository.save(키워드6_생성());
        Keyword 키워드7 = keywordRepository.save(키워드7_생성()); Keyword 키워드8 = keywordRepository.save(키워드8_생성());
        Keyword 키워드9 = keywordRepository.save(키워드9_생성()); Keyword 키워드10 = keywordRepository.save(키워드10_생성());
        tripKeywordRepository.save(new TripKeyword(여행지1, 키워드1)); tripKeywordRepository.save(new TripKeyword(여행지2, 키워드2));
        tripKeywordRepository.save(new TripKeyword(여행지3, 키워드3)); tripKeywordRepository.save(new TripKeyword(여행지4, 키워드4));
        tripKeywordRepository.save(new TripKeyword(여행지5, 키워드5)); tripKeywordRepository.save(new TripKeyword(여행지6, 키워드6));
        tripKeywordRepository.save(new TripKeyword(여행지7, 키워드7)); tripKeywordRepository.save(new TripKeyword(여행지8, 키워드8));
        tripKeywordRepository.save(new TripKeyword(여행지9, 키워드9)); tripKeywordRepository.save(new TripKeyword(여행지10, 키워드10));
        LiveInformation 생활정보1 = liveInformationRepository.save(생활정보1_생성());
        memberLiveInformationRepository.save(new MemberLiveInformation(생활정보1, 하온));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지1)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지2));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지3)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지4));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지5)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지6));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지7)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지8));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지9)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지10));

        // when, then
        assertThatThrownBy(() -> tripPreferredLocationsFilterStrategy.execute(new PreferredLocationsFilterInfo(하온.getId())))
                .isInstanceOf(LackOfRecommendTripException.class);
    }

    @DisplayName("관심 여행지로 회원가입 후 AI 맞춤 여행지를 추천받는다.")
    @Test
    void 관심_여행지로_회원가입_후_AI_맞춤_여행지를_추천받는다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        Trip 여행지1 = tripRepository.save(여행지1_생성()); Trip 여행지2 = tripRepository.save(여행지2_생성());
        Trip 여행지3 = tripRepository.save(여행지3_생성()); Trip 여행지4 = tripRepository.save(여행지4_생성());
        Trip 여행지5 = tripRepository.save(여행지5_생성()); Trip 여행지6 = tripRepository.save(여행지6_생성());
        Trip 여행지7 = tripRepository.save(여행지7_생성()); Trip 여행지8 = tripRepository.save(여행지8_생성());
        Trip 여행지9 = tripRepository.save(여행지9_생성()); Trip 여행지10 = tripRepository.save(여행지10_생성());

        Keyword 키워드1 = keywordRepository.save(키워드1_생성()); Keyword 키워드2 = keywordRepository.save(키워드2_생성());
        Keyword 키워드3 = keywordRepository.save(키워드3_생성()); Keyword 키워드4 = keywordRepository.save(키워드4_생성());
        Keyword 키워드5 = keywordRepository.save(키워드5_생성()); Keyword 키워드6 = keywordRepository.save(키워드6_생성());
        Keyword 키워드7 = keywordRepository.save(키워드7_생성()); Keyword 키워드8 = keywordRepository.save(키워드8_생성());
        Keyword 키워드9 = keywordRepository.save(키워드9_생성()); Keyword 키워드10 = keywordRepository.save(키워드10_생성());

        tripKeywordRepository.save(new TripKeyword(여행지1, 키워드1)); tripKeywordRepository.save(new TripKeyword(여행지2, 키워드2));
        tripKeywordRepository.save(new TripKeyword(여행지3, 키워드3)); tripKeywordRepository.save(new TripKeyword(여행지4, 키워드4));
        tripKeywordRepository.save(new TripKeyword(여행지5, 키워드5)); tripKeywordRepository.save(new TripKeyword(여행지6, 키워드6));
        tripKeywordRepository.save(new TripKeyword(여행지7, 키워드7)); tripKeywordRepository.save(new TripKeyword(여행지8, 키워드8));
        tripKeywordRepository.save(new TripKeyword(여행지9, 키워드9)); tripKeywordRepository.save(new TripKeyword(여행지10, 키워드10));

        LiveInformation 생활정보1 = liveInformationRepository.save(생활정보1_생성());
        memberLiveInformationRepository.save(new MemberLiveInformation(생활정보1, 하온));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지1)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지2));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지3)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지4));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지5)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지6));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지7)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지8));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지9)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지10));

        // when, then
        memberService.signUpByInterestTrips(하온.getId(), new SignUpInterestTripsRequest(List.of(1L, 2L, 3L, 4L, 5L)));
        List<Trip> actual = tripPreferredLocationsFilterStrategy.execute(new PreferredLocationsFilterInfo(하온.getId()));
        assertEquals(actual.size(), 10);
    }

    @DisplayName("여행지 추천에 필요한 회원의 생활정보를 선택하지 않았더라도, 관심 여행지로 회원가입 후 AI 맞춤 여행지를 추천받을 수 있다.")
    @Test
    void 여행지_추천에_필요한_회원의_생활정보롤_선택하지_않았더라도_관심_여행지로_회원가입_후_AI_맞춤_여행지를_추천받을_수_있다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        Trip 여행지1 = tripRepository.save(여행지1_생성()); Trip 여행지2 = tripRepository.save(여행지2_생성());
        Trip 여행지3 = tripRepository.save(여행지3_생성()); Trip 여행지4 = tripRepository.save(여행지4_생성());
        Trip 여행지5 = tripRepository.save(여행지5_생성()); Trip 여행지6 = tripRepository.save(여행지6_생성());
        Trip 여행지7 = tripRepository.save(여행지7_생성()); Trip 여행지8 = tripRepository.save(여행지8_생성());
        Trip 여행지9 = tripRepository.save(여행지9_생성()); Trip 여행지10 = tripRepository.save(여행지10_생성());

        Keyword 키워드1 = keywordRepository.save(키워드1_생성()); Keyword 키워드2 = keywordRepository.save(키워드2_생성());
        Keyword 키워드3 = keywordRepository.save(키워드3_생성()); Keyword 키워드4 = keywordRepository.save(키워드4_생성());
        Keyword 키워드5 = keywordRepository.save(키워드5_생성()); Keyword 키워드6 = keywordRepository.save(키워드6_생성());
        Keyword 키워드7 = keywordRepository.save(키워드7_생성()); Keyword 키워드8 = keywordRepository.save(키워드8_생성());
        Keyword 키워드9 = keywordRepository.save(키워드9_생성()); Keyword 키워드10 = keywordRepository.save(키워드10_생성());

        tripKeywordRepository.save(new TripKeyword(여행지1, 키워드1)); tripKeywordRepository.save(new TripKeyword(여행지2, 키워드2));
        tripKeywordRepository.save(new TripKeyword(여행지3, 키워드3)); tripKeywordRepository.save(new TripKeyword(여행지4, 키워드4));
        tripKeywordRepository.save(new TripKeyword(여행지5, 키워드5)); tripKeywordRepository.save(new TripKeyword(여행지6, 키워드6));
        tripKeywordRepository.save(new TripKeyword(여행지7, 키워드7)); tripKeywordRepository.save(new TripKeyword(여행지8, 키워드8));
        tripKeywordRepository.save(new TripKeyword(여행지9, 키워드9)); tripKeywordRepository.save(new TripKeyword(여행지10, 키워드10));

        LiveInformation 생활정보1 = liveInformationRepository.save(생활정보1_생성());
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지1)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지2));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지3)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지4));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지5)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지6));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지7)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지8));
        tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지9)); tripLiveInformationRepository.save(new TripLiveInformation(생활정보1, 여행지10));

        // when, then
        memberService.signUpByInterestTrips(하온.getId(), new SignUpInterestTripsRequest(List.of(1L, 2L, 3L, 4L, 5L)));
        List<Trip> actual = tripPreferredLocationsFilterStrategy.execute(new PreferredLocationsFilterInfo(하온.getId()));
        assertEquals(actual.size(), 10);
    }
}
