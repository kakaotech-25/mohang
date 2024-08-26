package moheng.planner.application;

import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.planner.domain.TripSchedule;
import moheng.planner.domain.TripScheduleRepository;
import moheng.planner.dto.FindPLannerOrderByNameResponse;
import moheng.planner.dto.FindPlannerOrderByDateResponse;
import moheng.planner.dto.FindPlannerOrderByRecentResponse;
import moheng.planner.dto.UpdateTripScheduleRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class PlannerService {
    private final TripScheduleRepository tripScheduleRepository;
    private final MemberRepository memberRepository;

    public PlannerService(final TripScheduleRepository tripScheduleRepository, final MemberRepository memberRepository) {
        this.tripScheduleRepository = tripScheduleRepository;
        this.memberRepository = memberRepository;
    }

    public FindPlannerOrderByRecentResponse findPlannerOrderByRecent(final long memberId) {
        final Member member = findMemberById(memberId);
        return new FindPlannerOrderByRecentResponse(tripScheduleRepository.findByMemberOrderByCreatedAtDesc(member));
    }

    public FindPlannerOrderByDateResponse findPlannerOrderByDateAsc(final long memberId) {
        final Member member = findMemberById(memberId);
        return new FindPlannerOrderByDateResponse(tripScheduleRepository.findByMemberOrderByStartDateAsc(member));
    }

    public FindPLannerOrderByNameResponse findPlannerOrderByName(final long memberId) {
        final Member member = findMemberById(memberId);
        return new FindPLannerOrderByNameResponse(tripScheduleRepository.findByMemberOrderByNameAsc(member));
    }

    @Transactional
    public void updateTripSchedule(final long memberId, final UpdateTripScheduleRequest updateTripScheduleRequest) {
        final Member member = findMemberById(memberId);
        final TripSchedule updateTripSchedule = new TripSchedule(
                updateTripScheduleRequest.getScheduleId(),
                updateTripScheduleRequest.getScheduleName(),
                updateTripScheduleRequest.getStartDate(),
                updateTripScheduleRequest.getStartDate(),
                member
        );
        tripScheduleRepository.save(updateTripSchedule);
    }

    private Member findMemberById(final long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoExistMemberException("존재하지 않는 유저입니다."));
    }
}
