package moheng.planner.application;

import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.planner.domain.TripScheduleRepository;
import moheng.planner.dto.FindPlannerOrderByDateResponse;
import moheng.planner.dto.FindPlannerOrderByRecentResponse;
import org.springframework.stereotype.Service;

@Service
public class PlannerService {
    private final TripScheduleRepository tripScheduleRepository;
    private final MemberRepository memberRepository;

    public PlannerService(final TripScheduleRepository tripScheduleRepository, final MemberRepository memberRepository) {
        this.tripScheduleRepository = tripScheduleRepository;
        this.memberRepository = memberRepository;
    }

    public FindPlannerOrderByRecentResponse findPlannerOrderByRecent(final long memberId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoExistMemberException("존재하지 않는 유저입니다."));
        return new FindPlannerOrderByRecentResponse(tripScheduleRepository.findByMemberOrderByCreatedAtDesc(member));
    }

    public FindPlannerOrderByDateResponse findPlannerOrderByDate(final long memberId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoExistMemberException("존재하지 않는 유저입니다."));
        return new FindPlannerOrderByDateResponse(tripScheduleRepository.findByMemberOrderByCreatedAtDesc(member));
    }
}
