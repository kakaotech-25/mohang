package moheng.recommendtrip.application;

import moheng.liveinformation.domain.MemberLiveInformationRepository;
import moheng.liveinformation.domain.TripLiveInformationRepository;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.RecommendTripRepository;
import moheng.recommendtrip.dto.RecommendTripCreateRequest;
import moheng.trip.domain.ExternalRecommendModelClient;
import moheng.trip.domain.Trip;
import moheng.trip.dto.RecommendTripsByVisitedLogsResponse;
import moheng.trip.exception.NoExistTripException;
import moheng.trip.domain.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class RecommendTripService {
    private static final int RECOMMEND_TRIPS_COUNT = 10;
    private final ExternalRecommendModelClient externalRecommendModelClient;
    private final RecommendTripRepository recommendTripRepository;
    private final MemberRepository memberRepository;
    private final TripRepository tripRepository;
    private final TripLiveInformationRepository tripLiveInformationRepository;
    private final MemberLiveInformationRepository memberLiveInformationRepository;

    public RecommendTripService(final ExternalRecommendModelClient externalRecommendModelClient,
                                final RecommendTripRepository recommendTripRepository,
                                final MemberRepository memberRepository,
                                final TripRepository tripRepository,
                                final TripLiveInformationRepository tripLiveInformationRepository,
                                final MemberLiveInformationRepository memberLiveInformationRepository) {
        this.externalRecommendModelClient = externalRecommendModelClient;
        this.recommendTripRepository = recommendTripRepository;
        this.memberRepository = memberRepository;
        this.tripRepository = tripRepository;
        this.tripLiveInformationRepository = tripLiveInformationRepository;
        this.memberLiveInformationRepository = memberLiveInformationRepository;
    }

    public void findRecommendTripsByVisitedLogs() {
        final RecommendTripsByVisitedLogsResponse recommendTripsByVisitedLogsResponse
                = externalRecommendModelClient.recommendTripsByVisitedLogs();
        filterTripsByLiveinformation(recommendTripsByVisitedLogsResponse);
    }

    // 여행지가 어떤 생활정보에 속하는지 DB 에 속하는지 저장해 놓아야한다.
    // 필터 : 추천받은 여행지의 생활정보 == 유저가 보유한 생활정보 리스트 중에 하나인 여행지라면 해당 여행지를 선택함
    private void filterTripsByLiveinformation(final RecommendTripsByVisitedLogsResponse recommendTripsByVisitedLogsResponse) {
    }

    @Transactional
    public void saveByRank(Trip trip, Member member, long rank) {
        recommendTripRepository.save(new RecommendTrip(trip, member, rank));
    }

    @Transactional
    public void createRecommendTrip(long memberId, RecommendTripCreateRequest request) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(NoExistMemberException::new);

        final Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(NoExistTripException::new);

        recommendTripRepository.save(new RecommendTrip(trip, member));
    }
}
