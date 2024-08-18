package moheng.recommendtrip.application;

import static moheng.fixture.MemberFixtures.하온_기존;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.ServiceTestConfig;
import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.KeywordRepository;
import moheng.keyword.domain.TripKeyword;
import moheng.keyword.domain.TripKeywordRepository;
import moheng.liveinformation.domain.*;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.recommendtrip.application.RecommendTripService;
import moheng.recommendtrip.dto.RecommendTripCreateRequest;
import moheng.trip.domain.MemberTrip;
import moheng.trip.domain.MemberTripRepository;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripResponse;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.exception.NoExistTripException;
import moheng.trip.domain.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RecommendTripServiceTest extends ServiceTestConfig {
    @Autowired
    private RecommendTripService recommendTripService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberTripRepository memberTripRepository;

    @Autowired
    private TripKeywordRepository tripKeywordRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private MemberLiveInformationRepository memberLiveInformationRepository;

    @Autowired
    private LiveInformationRepository liveInformationRepository;

    @Autowired
    private TripLiveInformationRepository tripLiveInformationRepository;

    @DisplayName("순위와 함께 추천 여행지 정보를 저장한다.")
    @Test
    void 순위와_함께_추천_여행지_정보를_저장한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip trip = tripRepository.save(new Trip("여행지", "장소명", 1L, "설명", "이미지 경로"));

        // when, then
        assertDoesNotThrow(() -> recommendTripService.saveByRank(trip, member, 1L));
    }

    @DisplayName("추천 여행지 정보를 저장한다.")
    @Test
    void 추천_여행지_정보를_저장한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip trip = tripRepository.save(new Trip("여행지", "장소명", 1L, "설명", "이미지 경로"));

        // when, then
        assertDoesNotThrow(() -> recommendTripService.createRecommendTrip(member.getId(), new RecommendTripCreateRequest(trip.getId())));
    }

    @DisplayName("존재하지 않는 여행지의 추천 정보를 저장하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_여행지의_추천_정보를_저장하면_예외가_발생한다() {
        // given
        Member member = memberRepository.save(하온_기존());

        // when, then
        assertThatThrownBy(() ->
                recommendTripService.createRecommendTrip(member.getId(), new RecommendTripCreateRequest(-1L))
        ).isInstanceOf(NoExistTripException.class);
    }

    @DisplayName("존재하지 않는 유저에 대한 추천 정보를 저장하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_유자에_대한_추천_정보를_저장하면_예외가_발생한다() {
        // given
        Trip trip = tripRepository.save(new Trip("여행지", "장소명", 1L, "설명", "이미지 경로"));

        // when, then
        assertThatThrownBy(() ->
                recommendTripService.createRecommendTrip(-1L, new RecommendTripCreateRequest(trip.getId()))
        ).isInstanceOf(NoExistMemberException.class);
    }

    @DisplayName("AI 맞춤 10개의 여행지를 추천받는다.")
    @Test
    void AI_맞춤_여행지를_추천받는다() {
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
        Keyword keyword1 = keywordRepository.save(new Keyword("키워드1")); Keyword keyword2 = keywordRepository.save(new Keyword("키워드2"));
        Keyword keyword3 = keywordRepository.save(new Keyword("키워드3")); Keyword keyword4 = keywordRepository.save(new Keyword("키워드4"));
        Keyword keyword5 = keywordRepository.save(new Keyword("키워드5")); Keyword keyword6 = keywordRepository.save(new Keyword("키워드6"));
        Keyword keyword7 = keywordRepository.save(new Keyword("키워드7")); Keyword keyword8 = keywordRepository.save(new Keyword("키워드8"));
        Keyword keyword9 = keywordRepository.save(new Keyword("키워드9")); Keyword keyword10 = keywordRepository.save(new Keyword("키워드10"));
        tripKeywordRepository.save(new TripKeyword(trip1, keyword1)); tripKeywordRepository.save(new TripKeyword(trip2, keyword2));
        tripKeywordRepository.save(new TripKeyword(trip3, keyword3)); tripKeywordRepository.save(new TripKeyword(trip4, keyword4));
        tripKeywordRepository.save(new TripKeyword(trip5, keyword5)); tripKeywordRepository.save(new TripKeyword(trip6, keyword6));
        tripKeywordRepository.save(new TripKeyword(trip7, keyword7)); tripKeywordRepository.save(new TripKeyword(trip8, keyword8));
        tripKeywordRepository.save(new TripKeyword(trip9, keyword9)); tripKeywordRepository.save(new TripKeyword(trip10, keyword10));
        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        memberLiveInformationRepository.save(new MemberLiveInformation(liveInformation, member));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip1)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10));

        // when, then
        assertAll(() -> {
            assertDoesNotThrow(() -> recommendTripService.findRecommendTripsByModel(member.getId()));
            assertThat(recommendTripService.findRecommendTripsByModel(member.getId()).getFindTripResponses()).hasSize(10);
        });
    }

    @DisplayName("존재하지 않는 멤버기 AI 맞춤 10개의 여행지를 추천받으면 예외가 발생한다.")
    @Test
    void 존재하지_않는_멤버가_AI_맞춤_여행지를_추천받으면_예외가_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> recommendTripService.findRecommendTripsByModel(-1L))
                .isInstanceOf(NoExistMemberException.class);
    }

    @DisplayName("AI 맞춤 여행지는 멤버의 생활정보로 필터링된다.")
    @Test
    void AI_맞춤_여행지는_멤버의_생활정보로_필터링된다() {
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
        Trip trip11 = tripRepository.save(new Trip("멤버가 관심없는 생활정보에 대한 여행지", "장소명3", 11L, "설명3", "이미지 경로3"));
        memberTripRepository.save(new MemberTrip(member, trip1, 10L)); memberTripRepository.save(new MemberTrip(member, trip2, 20L));
        memberTripRepository.save(new MemberTrip(member, trip3, 30L)); memberTripRepository.save(new MemberTrip(member, trip4, 30L));
        memberTripRepository.save(new MemberTrip(member, trip5, 30L)); memberTripRepository.save(new MemberTrip(member, trip6, 30L));
        memberTripRepository.save(new MemberTrip(member, trip7, 30L)); memberTripRepository.save(new MemberTrip(member, trip8, 30L));
        memberTripRepository.save(new MemberTrip(member, trip9, 30L)); memberTripRepository.save(new MemberTrip(member, trip10, 30L));
        memberTripRepository.save(new MemberTrip(member, trip11, 30L));
        Keyword keyword1 = keywordRepository.save(new Keyword("키워드1")); Keyword keyword2 = keywordRepository.save(new Keyword("키워드2"));
        Keyword keyword3 = keywordRepository.save(new Keyword("키워드3")); Keyword keyword4 = keywordRepository.save(new Keyword("키워드4"));
        Keyword keyword5 = keywordRepository.save(new Keyword("키워드5")); Keyword keyword6 = keywordRepository.save(new Keyword("키워드6"));
        Keyword keyword7 = keywordRepository.save(new Keyword("키워드7")); Keyword keyword8 = keywordRepository.save(new Keyword("키워드8"));
        Keyword keyword9 = keywordRepository.save(new Keyword("키워드9")); Keyword keyword10 = keywordRepository.save(new Keyword("키워드10"));
        Keyword keyword11 = keywordRepository.save(new Keyword("멤버가 관심없는 생활정보 관련 여행지의 키워드"));
        tripKeywordRepository.save(new TripKeyword(trip1, keyword1)); tripKeywordRepository.save(new TripKeyword(trip2, keyword2));
        tripKeywordRepository.save(new TripKeyword(trip3, keyword3)); tripKeywordRepository.save(new TripKeyword(trip4, keyword4));
        tripKeywordRepository.save(new TripKeyword(trip5, keyword5)); tripKeywordRepository.save(new TripKeyword(trip6, keyword6));
        tripKeywordRepository.save(new TripKeyword(trip7, keyword7)); tripKeywordRepository.save(new TripKeyword(trip8, keyword8));
        tripKeywordRepository.save(new TripKeyword(trip9, keyword9)); tripKeywordRepository.save(new TripKeyword(trip10, keyword10));
        tripKeywordRepository.save(new TripKeyword(trip10, keyword11));
        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        LiveInformation otherLiveInformation = liveInformationRepository.save(new LiveInformation("멤버가 관심없는 다른 생활정보"));
        memberLiveInformationRepository.save(new MemberLiveInformation(liveInformation, member));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip1)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10));
        tripLiveInformationRepository.save(new TripLiveInformation(otherLiveInformation, trip11));

        // when
        List<FindTripResponse> tripResponses = recommendTripService.findRecommendTripsByModel(member.getId()).getFindTripResponses();

        // then
        assertAll(() -> {
            assertThat(tripResponses).hasSize(10);
            assertThat(tripResponses.stream()
                    .anyMatch(trip -> trip.getContentId().equals(11L))).isFalse();
        });
    }
}
