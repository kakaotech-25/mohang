package moheng.trip.application;

import moheng.keyword.domain.TripKeyword;
import moheng.keyword.domain.TripKeywordRepository;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.RecommendTripRepository;
import moheng.trip.domain.*;
import moheng.trip.domain.recommendstrategy.SaveRecommendTripStrategyProvider;
import moheng.trip.dto.*;
import moheng.trip.exception.NoExistRecommendTripException;
import moheng.trip.exception.NoExistTripException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class TripService {
    private static final long RECOMMEND_TRIPS_SIZE = 10L;
    private static final long HIGHEST_PRIORITY_RANK = 1L;
    private static final long LOWEST_PRIORITY_RANK = 10L;
    private final ExternalSimilarTripModelClient externalSimilarTripModelClient;
    private final SaveRecommendTripStrategyProvider saveRecommendTripStrategyProvider;
    private final TripRepository tripRepository;
    private final RecommendTripRepository recommendTripRepository;
    private final MemberRepository memberRepository;
    private final MemberTripRepository memberTripRepository;
    private final TripKeywordRepository tripKeywordRepository;

    public TripService(final TripRepository tripRepository,
                       final ExternalSimilarTripModelClient externalSimilarTripModelClient,
                       final RecommendTripRepository recommendTripRepository,
                       final MemberRepository memberRepository,
                       final MemberTripRepository memberTripRepository, TripKeywordRepository tripKeywordRepository,
                       final SaveRecommendTripStrategyProvider saveRecommendTripStrategyProvider) {
        this.tripRepository = tripRepository;
        this.externalSimilarTripModelClient = externalSimilarTripModelClient;
        this.recommendTripRepository = recommendTripRepository;
        this.memberRepository = memberRepository;
        this.memberTripRepository = memberTripRepository;
        this.tripKeywordRepository = tripKeywordRepository;
        this.saveRecommendTripStrategyProvider = saveRecommendTripStrategyProvider;
    }

    @Transactional
    public FindTripWithSimilarTripsResponse findWithSimilarOtherTrips(final long tripId, final long memberId) {
        final Trip trip = findById(tripId);
        final FindSimilarTripWithContentIdResponses similarTripWithContentIdResponses = externalSimilarTripModelClient.findSimilarTrips(tripId);
        final SimilarTripResponses similarTripResponses = findTripsByContentIdsWithKeywords(similarTripWithContentIdResponses.getContentIds());
        trip.incrementVisitedCount();
        saveRecommendTripByClickedLogs(memberId, trip);
        return new FindTripWithSimilarTripsResponse(trip, findKeywordsByTrip(trip), similarTripResponses);
    }

    private List<String> findKeywordsByTrip(final Trip trip) {
        return tripKeywordRepository.findByTrip(trip)
                .stream()
                .map(tripKeyword -> tripKeyword.getKeyword().getName())
                .collect(Collectors.toList());
    }

    private SimilarTripResponses findTripsByContentIdsWithKeywords(final List<Long> contentIds) {
        final List<Trip> trips = contentIds.stream()
                .map(this::findByContentId)
                .collect(Collectors.toList());
        final List<TripKeyword> tripKeywords = tripKeywordRepository.findByTrips(trips);
        return new SimilarTripResponses(trips, tripKeywords);
    }

    private void saveRecommendTripByClickedLogs(final long memberId, final Trip trip) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(NoExistMemberException::new);
        final List<RecommendTrip> recommendTrips = recommendTripRepository.findTop10ByMember(member);

        final MemberTrip memberTrip = findOrCreateMemberTrip(member, trip);
        memberTrip.incrementVisitedCount();

        if(recommendTrips.size() < RECOMMEND_TRIPS_SIZE) {
            final long highestRank = findHighestRecommendTripRank(recommendTrips);
            recommendTripRepository.save(new RecommendTrip(trip, member, highestRank + 1));
        }
        else if(recommendTrips.size() >= RECOMMEND_TRIPS_SIZE) {
            if(!recommendTripRepository.existsByMemberAndTrip(member, trip)) {
                downAllRanks(recommendTrips);
                deleteHighestPriorityRankRecommendTrip(member);
                recommendTripRepository.save(new RecommendTrip(trip, member, LOWEST_PRIORITY_RANK));
            }
        }
    }

    private void deleteHighestPriorityRankRecommendTrip(final Member member) {
        if(!recommendTripRepository.existsByMemberAndRank(member, HIGHEST_PRIORITY_RANK - 1)) {
            throw new NoExistRecommendTripException("존재하지 않는 선호 여행지 정보입니다.");
        }
        recommendTripRepository.deleteByMemberAndRank(member, HIGHEST_PRIORITY_RANK - 1);
    }

    private void downAllRanks(List<RecommendTrip> recommendTrips) {
        recommendTripRepository.bulkDownRank(recommendTrips);
    }

    private MemberTrip findOrCreateMemberTrip(final Member member, final Trip trip) {
        if(!memberTripRepository.existsByMemberAndTrip(member, trip)) {
            memberTripRepository.save(new MemberTrip(member, trip));
        }
        final MemberTrip foundMemberTrip = memberTripRepository.findByMemberAndTrip(member, trip);
        return foundMemberTrip;
    }

    private long findHighestRecommendTripRank(final List<RecommendTrip> recommendTrips) {
        return recommendTrips.stream()
                .max(Comparator.comparing(RecommendTrip::getRank))
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
        final List<Trip> topTrips = tripRepository.findTop30ByOrderByVisitedCountDesc();
        final List<TripKeyword> tripKeywords = tripKeywordRepository.findByTrips(topTrips);
        return new FindTripsResponse(tripKeywords);
    }
}
