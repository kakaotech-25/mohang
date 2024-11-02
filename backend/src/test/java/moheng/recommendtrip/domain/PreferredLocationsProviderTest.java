package moheng.recommendtrip.domain;

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
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.recommendtrip.domain.preferredlocation.PreferredLocationsByFilterFinder;
import moheng.recommendtrip.domain.repository.RecommendTripRepository;
import moheng.recommendtrip.exception.LackOfRecommendTripException;
import moheng.trip.domain.MemberTrip;
import moheng.trip.domain.repository.MemberTripRepository;
import moheng.trip.domain.Trip;
import moheng.trip.domain.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PreferredLocationsProviderTest extends ServiceTestConfig {
    @Autowired
    private PreferredLocationsByFilterFinder preferredLocationsProvider;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberTripRepository memberTripRepository;

    @Autowired
    private RecommendTripRepository recommendTripRepository;

    @DisplayName("멤버의 선호 여행지를 가져온다.")
    @Test
    void 멤버의_선호_여행지를_가져온다() {
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

        // when, then
        assertDoesNotThrow(() -> preferredLocationsProvider.findPreferredLocations(하온.getId()));
    }

    @DisplayName("추천을 받기위한 멤버의 선호 여행지 개수가 5개 미만으로 부족하면 예외가 발생한다.")
    @Test
    void 추천을_받기위한_멤버의_선호_여행지_개수가_5개_미만으로_부족하면_예외가_발생한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip 여행지1 = tripRepository.save(여행지1_생성()); Trip 여행지2 = tripRepository.save(여행지2_생성());
        Trip 여행지3 = tripRepository.save(여행지3_생성()); Trip 여행지4 = tripRepository.save(여행지4_생성());
        memberTripRepository.save(new MemberTrip(member, 여행지1, 10L)); memberTripRepository.save(new MemberTrip(member, 여행지2, 20L));
        memberTripRepository.save(new MemberTrip(member, 여행지3, 30L)); memberTripRepository.save(new MemberTrip(member, 여행지4, 30L));
        recommendTripRepository.save(new RecommendTrip(여행지1, member, 1L)); recommendTripRepository.save(new RecommendTrip(여행지2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(여행지3, member, 3L)); recommendTripRepository.save(new RecommendTrip(여행지4, member, 4L));

        // when, then
        assertThatThrownBy(() -> preferredLocationsProvider.findPreferredLocations(member.getId()))
                .isInstanceOf(LackOfRecommendTripException.class);
    }
}
