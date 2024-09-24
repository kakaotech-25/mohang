package moheng.trip.application;

import jakarta.persistence.LockTimeoutException;
import moheng.keyword.domain.TripKeyword;
import moheng.keyword.domain.repository.TripKeywordRepository;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.repository.RecommendTripRepository;
import moheng.recommendtrip.domain.filterinfo.LiveInformationFilterInfo;
import moheng.recommendtrip.domain.tripfilterstrategy.TripFilterStrategy;
import moheng.recommendtrip.domain.tripfilterstrategy.TripFilterStrategyProvider;
import moheng.trip.domain.*;
import moheng.trip.domain.recommendstrategy.RecommendTripStrategy;
import moheng.trip.domain.recommendstrategy.SaveRecommendTripStrategyProvider;
import moheng.trip.domain.repository.MemberTripRepository;
import moheng.trip.domain.repository.TripRepository;
import moheng.trip.dto.*;
import moheng.trip.exception.NoExistTripException;
import moheng.trip.exception.NotAbleToAccessTripByPessimisticLock;
import org.hibernate.PessimisticLockException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class TripService {
    private static final String LIVE_INFO_STRATEGY = "LIVE_INFO";
    private final TripFilterStrategyProvider tripFilterStrategyProvider;
    private final SaveRecommendTripStrategyProvider saveRecommendTripStrategyProvider;
    private final TripRepository tripRepository;
    private final RecommendTripRepository recommendTripRepository;
    private final MemberRepository memberRepository;
    private final MemberTripRepository memberTripRepository;
    private final TripKeywordRepository tripKeywordRepository;

    public TripService(final TripFilterStrategyProvider tripFilterStrategyProvider,
                       final TripRepository tripRepository,
                       final RecommendTripRepository recommendTripRepository,
                       final MemberRepository memberRepository,
                       final MemberTripRepository memberTripRepository,
                       final TripKeywordRepository tripKeywordRepository,
                       final SaveRecommendTripStrategyProvider saveRecommendTripStrategyProvider) {
        this.tripFilterStrategyProvider = tripFilterStrategyProvider;
        this.tripRepository = tripRepository;
        this.recommendTripRepository = recommendTripRepository;
        this.memberRepository = memberRepository;
        this.memberTripRepository = memberTripRepository;
        this.tripKeywordRepository = tripKeywordRepository;
        this.saveRecommendTripStrategyProvider = saveRecommendTripStrategyProvider;
    }

    @Transactional
    public FindTripWithSimilarTripsResponse findWithSimilarOtherTrips(final long tripId, final long memberId) {
        try {
            final Trip trip = tripRepository.findByIdForUpdate(tripId)
                    .orElseThrow(NoExistTripException::new);
            final TripFilterStrategy tripFilterStrategy = tripFilterStrategyProvider.findTripsByFilterStrategy(LIVE_INFO_STRATEGY);
            final List<Trip> filteredSimilarTrips = tripFilterStrategy.execute(new LiveInformationFilterInfo(tripId));

            trip.incrementVisitedCount();
            saveRecommendTripByClickedLogs(memberId, trip);

            return new FindTripWithSimilarTripsResponse(trip, findKeywordsByTrip(trip), findTripsWithKeywords(filteredSimilarTrips));
        } catch (PessimisticLockException e) {
            throw new NotAbleToAccessTripByPessimisticLock("접속자 수가 많아 여행지 조회가 불가능합니다. 잠시 후에 시도해주세요.");
        }
    }

    private SimilarTripResponses findTripsWithKeywords(final List<Trip> trips) {
        final List<TripKeyword> tripKeywords = tripKeywordRepository.findByTrips(trips);
        return new SimilarTripResponses(trips, tripKeywords);
    }

    private void saveRecommendTripByClickedLogs(final long memberId, final Trip trip) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(NoExistMemberException::new);
        final List<RecommendTrip> recommendTrips = recommendTripRepository.findByMemberOrderByRankingDesc(member);
        final MemberTrip memberTrip = findOrCreateMemberTrip(member, trip);
        memberTrip.incrementVisitedCount();
        saveRecommendLogsByStrategy(trip, member, recommendTrips);
    }

    private MemberTrip findOrCreateMemberTrip(final Member member, final Trip trip) {
        if(!memberTripRepository.existsByMemberAndTrip(member, trip)) {
            memberTripRepository.save(new MemberTrip(member, trip));
        }
        final MemberTrip foundMemberTrip = memberTripRepository.findByMemberAndTrip(member, trip);
        return foundMemberTrip;
    }

    private void saveRecommendLogsByStrategy(final Trip trip, final Member member, final List<RecommendTrip> recommendTrips) {
        final RecommendTripStrategy recommendTripStrategy = saveRecommendTripStrategyProvider.findRecommendTripStrategy(recommendTrips.size());
        recommendTripStrategy.execute(trip, member, recommendTrips);
    }

    private List<String> findKeywordsByTrip(final Trip trip) {
        return tripKeywordRepository.findByTrip(trip)
                .stream()
                .map(tripKeyword -> tripKeyword.getKeyword().getName())
                .collect(Collectors.toList());
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
        return new FindTripsResponse(findSortedTripKeywords(topTrips, tripKeywords));
    }

    private List<TripKeyword> findSortedTripKeywords(final List<Trip> topTrips, final List<TripKeyword> tripKeywords) {
        return topTrips.stream()
                .flatMap(topTrip -> tripKeywords.stream()
                        .filter(tripKeyword -> tripKeyword.getTrip().equals(topTrip)))
                .collect(Collectors.toList());
    }

    public void createMemberTrip(final long memberId, final long tripId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(NoExistMemberException::new);

        final Trip trip = tripRepository.findById(tripId)
                .orElseThrow(NoExistTripException::new);

        memberTripRepository.save(new MemberTrip(member, trip));
    }
}
