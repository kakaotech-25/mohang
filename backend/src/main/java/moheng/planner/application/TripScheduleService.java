package moheng.planner.application;

import moheng.member.domain.repository.MemberRepository;
import moheng.planner.domain.TripScheduleRepository;
import org.springframework.stereotype.Service;

@Service
public class TripScheduleService {
    private final MemberRepository memberRepository;
    private final TripScheduleRepository tripScheduleRepository;

    public TripScheduleService(final MemberRepository memberRepository, final TripScheduleRepository tripScheduleRepository) {
        this.memberRepository = memberRepository;
        this.tripScheduleRepository = tripScheduleRepository;
    }

    public void createPlan() {

    }
}
