package moheng.recommendtrip.application;

import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.MemberLiveInformationRepository;
import moheng.liveinformation.domain.TripLiveInformationRepository;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.RecommendTripRepository;
import moheng.recommendtrip.dto.RecommendTripCreateRequest;
import moheng.trip.domain.*;
import moheng.trip.dto.RecommendTripsByVisitedLogsRequest;
import moheng.trip.dto.RecommendTripsByVisitedLogsResponse;
import moheng.trip.exception.NoExistTripException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final MemberTripRepository memberTripRepository;

    public RecommendTripService(final ExternalRecommendModelClient externalRecommendModelClient,
                                final RecommendTripRepository recommendTripRepository,
                                final MemberRepository memberRepository,
                                final TripRepository tripRepository,
                                final TripLiveInformationRepository tripLiveInformationRepository,
                                final MemberLiveInformationRepository memberLiveInformationRepository,
                                final MemberTripRepository memberTripRepository) {
        this.externalRecommendModelClient = externalRecommendModelClient;
        this.recommendTripRepository = recommendTripRepository;
        this.memberRepository = memberRepository;
        this.tripRepository = tripRepository;
        this.tripLiveInformationRepository = tripLiveInformationRepository;
        this.memberLiveInformationRepository = memberLiveInformationRepository;
        this.memberTripRepository = memberTripRepository;
    }

    public void findRecommendTripsByVisitedLogs(long memberId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(NoExistMemberException::new);
        final Map<Long, Long> preferredLocations = findMemberPreferredLocations(memberTripRepository.findByMember(member));
        long page = 1L;
        final List<Trip> finalFilteredTrips = new ArrayList<>();
        while (finalFilteredTrips.size() < RECOMMEND_TRIPS_COUNT) {
            final RecommendTripsByVisitedLogsResponse recommendTripsByVisitedLogsResponse =
                    externalRecommendModelClient.recommendTripsByVisitedLogs(
                            new RecommendTripsByVisitedLogsRequest(preferredLocations, page)
                    );
            final List<Trip> filteredTripsByLiveinformation =
                    filterTripsByLiveinformation(recommendTripsByVisitedLogsResponse, memberId);
            finalFilteredTrips.addAll(filteredTripsByLiveinformation);
            page++;
        }
    }

    private Map<Long, Long> findMemberPreferredLocations(List<MemberTrip> memberTrips) {
        return memberTrips.stream()
                .collect(Collectors.toMap(trip -> trip.getTrip().getContentId(), MemberTrip::getVisitedCount));
    }

    private List<Trip> filterTripsByLiveinformation(final RecommendTripsByVisitedLogsResponse recommendTripsByVisitedLogsResponse, final Long memberId) {
        final List<Trip> trips = tripRepository.findTripsByContentIds(recommendTripsByVisitedLogsResponse.getContentIds());
        final List<LiveInformation> memberLiveInformations = memberLiveInformationRepository.findLiveInformationsByMemberId(memberId);
        return filterTripsByMemberInformation(trips, memberLiveInformations);
    }

    private List<Trip> filterTripsByMemberInformation(final List<Trip> trips, final List<LiveInformation> memberLiveInformations) {
        return trips.stream()
                .filter(trip -> tripLiveInformationRepository.existsByTripAndLiveInformationIn(trip, memberLiveInformations))
                .collect(Collectors.toList());
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
