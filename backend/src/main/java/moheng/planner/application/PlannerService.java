package moheng.planner.application;

import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.planner.domain.TripSchedule;
import moheng.planner.domain.repository.TripScheduleRegistryRepository;
import moheng.planner.domain.repository.TripScheduleRepository;
import moheng.planner.dto.FindPLannerOrderByNameResponse;
import moheng.planner.dto.FindPlannerOrderByDateResponse;
import moheng.planner.dto.FindPlannerOrderByRecentResponse;
import moheng.planner.dto.UpdateTripScheduleRequest;
import moheng.planner.exception.AlreadyExistTripScheduleException;
import moheng.planner.exception.NoExistTripScheduleException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class PlannerService {
    private final TripScheduleRepository tripScheduleRepository;
    private final MemberRepository memberRepository;
    private final TripScheduleRegistryRepository tripScheduleRegistryRepository;

    public PlannerService(final TripScheduleRepository tripScheduleRepository,
                          final MemberRepository memberRepository,
                          final TripScheduleRegistryRepository tripScheduleRegistryRepository) {
        this.tripScheduleRepository = tripScheduleRepository;
        this.memberRepository = memberRepository;
        this.tripScheduleRegistryRepository = tripScheduleRegistryRepository;
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
        final TripSchedule tripSchedule = tripScheduleRepository.findById(updateTripScheduleRequest.getScheduleId())
                .orElseThrow(NoExistTripScheduleException::new);

        if(tripSchedule.isNameChanged(updateTripScheduleRequest.getScheduleName())) {
            checkIsDuplicateMemberScheduleName(member, updateTripScheduleRequest.getScheduleName());
        }

        final TripSchedule updateTripSchedule = new TripSchedule(
                updateTripScheduleRequest.getScheduleId(),
                updateTripScheduleRequest.getScheduleName(),
                updateTripScheduleRequest.getStartDate(),
                updateTripScheduleRequest.getStartDate(),
                member
        );
        tripScheduleRepository.save(updateTripSchedule);
    }

    private void checkIsDuplicateMemberScheduleName(final Member member, final String scheduleName) {
        if(tripScheduleRepository.existsByMemberAndName(member, scheduleName)) {
            throw new AlreadyExistTripScheduleException("동일한 이름의 여행 일정이 이미 존재합니다.");
        }
    }

    private Member findMemberById(final long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoExistMemberException("존재하지 않는 유저입니다."));
    }

    @Transactional
    public void removeTripSchedule(final Long tripScheduleId) {
        if(!tripScheduleRepository.existsById(tripScheduleId)) {
            throw new NoExistTripScheduleException("존재하지 않는 여행 일정입니다.");
        }
        tripScheduleRegistryRepository.deleteAllByTripScheduleId(tripScheduleId);
        tripScheduleRepository.deleteById(tripScheduleId);
    }
}
