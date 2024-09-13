package moheng.fixture;

import moheng.member.domain.Member;
import moheng.trip.domain.MemberTrip;
import moheng.trip.domain.Trip;

public class MemberTripFixture {
    public static MemberTrip 멤버의_여행지_방문수_10_생성(Member member, Trip trip) {
        return new MemberTrip(member, trip, 10L);
    }

    public static MemberTrip 멤버의_여행지_방문수_20_생성(Member member, Trip trip) {
        return new MemberTrip(member, trip, 20L);
    }

    public static MemberTrip 멤버의_여행지_방문수_30_생성(Member member, Trip trip) {
        return new MemberTrip(member, trip, 30L);
    }
}
