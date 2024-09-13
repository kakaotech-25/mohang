package moheng.recommendtrip.domain;

import static moheng.config.fixture.MemberFixtures.하온_기존;
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

        // when, then
        assertDoesNotThrow(() -> preferredLocationsProvider.findPreferredLocations(member.getId()));
    }

    @DisplayName("추천을 받기위한 멤버의 선호 여행지 개수가 5개 미만으로 부족하면 예외가 발생한다.")
    @Test
    void 추천을_받기위한_멤버의_선호_여행지_개수가_5개_미만으로_부족하면_예외가_발생한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip trip1 = tripRepository.save(new Trip("여행지1", "장소명1", 1L, "설명1", "이미지 경로1"));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "장소명2", 2L, "설명2", "이미지 경로2"));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "장소명3", 3L, "설명3", "이미지 경로3"));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "장소명3", 4L, "설명3", "이미지 경로3"));
        memberTripRepository.save(new MemberTrip(member, trip1, 10L)); memberTripRepository.save(new MemberTrip(member, trip2, 20L));
        memberTripRepository.save(new MemberTrip(member, trip3, 30L)); memberTripRepository.save(new MemberTrip(member, trip4, 30L));
        recommendTripRepository.save(new RecommendTrip(trip1, member, 1L)); recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 3L)); recommendTripRepository.save(new RecommendTrip(trip4, member, 4L));

        // when, then
        assertThatThrownBy(() -> preferredLocationsProvider.findPreferredLocations(member.getId()))
                .isInstanceOf(LackOfRecommendTripException.class);
    }
}
