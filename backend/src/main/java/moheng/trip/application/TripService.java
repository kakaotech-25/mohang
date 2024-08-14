package moheng.trip.application;

import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.RecommendTripRepository;
import moheng.trip.domain.ExternalSimilarTripModelClient;
import moheng.trip.domain.Trip;
import moheng.trip.dto.*;
import moheng.trip.exception.InvalidRecommendTripRankException;
import moheng.trip.exception.NoExistRecommendTripException;
import moheng.trip.exception.NoExistTripException;
import moheng.trip.domain.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class TripService {
    private static final long RECOMMEND_TRIPS_SIZE = 10;
    private static final long HIGHEST_PRIORITY_RANK = 1L;
    private static final long LOWEST_PRIORITY_RANK = 1999999999L;
    private final ExternalSimilarTripModelClient externalSimilarTripModelClient;
    private final TripRepository tripRepository;
    private final RecommendTripRepository recommendTripRepository;
    private final MemberRepository memberRepository;

    public TripService(final TripRepository tripRepository,
                       final ExternalSimilarTripModelClient externalSimilarTripModelClient,
                       final RecommendTripRepository recommendTripRepository, MemberRepository memberRepository) {
        this.tripRepository = tripRepository;
        this.externalSimilarTripModelClient = externalSimilarTripModelClient;
        this.recommendTripRepository = recommendTripRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public FindTripWithSimilarTripsResponse findWithSimilarOtherTrips(final long tripId, final long memberId) {
        final Trip trip = findById(tripId);
        final FindSimilarTripWithContentIdResponses similarTripWithContentIdResponses = externalSimilarTripModelClient.findSimilarTrips(tripId);
        final SimilarTripResponses similarTripResponses = findTripsByContentIds(similarTripWithContentIdResponses.getContentIds());
        trip.incrementVisitedCount();
        saveRecommendTripByClickedLogs(memberId, trip);
        return new FindTripWithSimilarTripsResponse(trip, similarTripResponses);
    }

    private SimilarTripResponses findTripsByContentIds(final List<Long> contentIds) {
        final List<Trip> trips = contentIds.stream()
                .map(this::findByContentId)
                .collect(Collectors.toList());
        return new SimilarTripResponses(trips);
    }

    private void saveRecommendTripByClickedLogs(final long memberId, final Trip trip) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(NoExistMemberException::new);
        final List<RecommendTrip> recommendTrips = recommendTripRepository.findTop10ByMemberOrderByVisitedCountDesc(member);

        if(recommendTrips.size() < RECOMMEND_TRIPS_SIZE) {
            final long highestRank = findHighestRecommendTripRank(recommendTrips);
            recommendTripRepository.save(new RecommendTrip(trip, member, highestRank + 1));
        }
        else if(recommendTrips.size() >= RECOMMEND_TRIPS_SIZE) {
            downAllRanks(recommendTrips);
            changeHighestPriorityRankToLowest(member);
            final RecommendTrip foundRecommendTrip = findOrCreateRecommendTrip(member, trip);
            foundRecommendTrip.changeRank(RECOMMEND_TRIPS_SIZE);
            foundRecommendTrip.increaseVisitedCount();
        }
    }

    private void changeHighestPriorityRankToLowest(final Member member) {
        final RecommendTrip highestPriorityRankTrip = recommendTripRepository.findByMemberAndRank(member, HIGHEST_PRIORITY_RANK-1)
                .orElseThrow(() -> new InvalidRecommendTripRankException("여행지의 우선순위 값이 유효하지 않습니다."));
        highestPriorityRankTrip.changeRank(LOWEST_PRIORITY_RANK);
    }

    private void downAllRanks(List<RecommendTrip> recommendTrips) {
        recommendTripRepository.bulkDownRank(recommendTrips);
    }

    private RecommendTrip findOrCreateRecommendTrip(final Member member, final Trip trip) {
        if(!recommendTripRepository.existsByMemberAndTrip(member, trip)) {
            recommendTripRepository.save(new RecommendTrip(trip, member, HIGHEST_PRIORITY_RANK));
        }
        final RecommendTrip foundRecommendTrip = recommendTripRepository.findByMemberAndTrip(member, trip);
        return foundRecommendTrip;
    }

    private long findHighestRecommendTripRank(final List<RecommendTrip> recommendTrips) {
        return recommendTrips.stream()
                .max(Comparator.comparing(RecommendTrip::getRank))
                .map(RecommendTrip::getRank)
                .orElseThrow(() -> new NoExistRecommendTripException("선호 여행지 정보가 없습니다."));
    }

    private long findHLowestRecommendTripRank(List<RecommendTrip> recommendTrips) {
        return recommendTrips.stream()
                .min(Comparator.comparing(RecommendTrip::getRank))
                .map(RecommendTrip::getRank)
                .orElseThrow(() -> new NoExistRecommendTripException("선호 여행지 정보가 없습니다."));
    }


    public Trip findByContentId(final Long contentId) {
        final Trip trip = tripRepository.findByContentId(contentId)
                .orElseThrow(NoExistTripException::new);
        return trip;
    }

    public Trip findById(final Long tripId) {
        final Trip trip = tripRepository.findById(tripId)
                .orElseThrow(NoExistTripException::new);
        return trip;
    }

    @Transactional
    public void createTrip(final TripCreateRequest tripCreateRequest) {
        final Trip trip = new Trip(
                tripCreateRequest.getName(),
                tripCreateRequest.getPlaceName(),
                tripCreateRequest.getContentId(),
                tripCreateRequest.getDescription(),
                tripCreateRequest.getTripImageUrl()
        );
        tripRepository.save(trip);
    }

    @Transactional
    public void save(final Trip trip) {
        tripRepository.save(trip);
    }

    public FindTripsResponse findTop30OrderByVisitedCountDesc() {
        return new FindTripsResponse(tripRepository.findTop30ByOrderByVisitedCountDesc());
    }
}
