package moheng.recommendtrip.domain;

import moheng.keyword.dto.RecommendTripResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecommendTripRepository extends JpaRepository<RecommendTrip, Long> {
    @Query("SELECT new moheng.keyword.dto.RecommendTripResponse(rt.visitedCount, rt.trip.contentId) FROM RecommendTrip rt WHERE rt.member.id = :memberId")
    List<RecommendTripResponse> findVisitedCountAndTripContentIdByMemberId(@Param("memberId") Long memberId);
}

