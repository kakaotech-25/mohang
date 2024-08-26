package moheng.planner.application;

import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.planner.domain.TripSchedule;
import moheng.planner.domain.TripScheduleRepository;
import moheng.planner.dto.CreateTripScheduleRequest;
import moheng.planner.exception.AlreadyExistTripScheduleException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TripScheduleService {
    private final MemberRepository memberRepository;
    private final TripScheduleRepository tripScheduleRepository;

    public TripScheduleService(final MemberRepository memberRepository, final TripScheduleRepository tripScheduleRepository) {
        this.memberRepository = memberRepository;
        this.tripScheduleRepository = tripScheduleRepository;
    }

    @Transactional
    public void createTripSchedule(final long memberId, final CreateTripScheduleRequest createTripScheduleRequest) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoExistMemberException("존재하지 않는 유저입니다."));

        if(tripScheduleRepository.existsByMemberAndName(member, createTripScheduleRequest.getScheduleName())) {
            throw new AlreadyExistTripScheduleException("동일한 이름의 여행 일정이 이미 존재합니다.");
        }

        final TripSchedule tripSchedule = new TripSchedule(
                createTripScheduleRequest.getScheduleName(),
                createTripScheduleRequest.getStartDate(),
                createTripScheduleRequest.getEndDate(),
                member
        );
        tripScheduleRepository.save(tripSchedule);
    }

    @Transactional
    public void removeTripSchedule(final Long tripScheduleId) {
        tripScheduleRepository.deleteById(tripScheduleId);
    }
}
