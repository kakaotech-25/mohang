package moheng.planner.application;

import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.planner.domain.TripSchedule;
import moheng.planner.domain.TripScheduleRegistry;
import moheng.planner.domain.repository.TripScheduleRegistryRepository;
import moheng.planner.domain.repository.TripScheduleRepository;
import moheng.planner.dto.request.AddTripOnScheduleRequests;
import moheng.planner.dto.request.CreateTripScheduleRequest;
import moheng.planner.dto.request.UpdateTripOrdersRequest;
import moheng.planner.dto.response.FindTripsOnSchedule;
import moheng.planner.exception.AlreadyExistTripScheduleException;
import moheng.planner.exception.NoExistTripScheduleException;
import moheng.planner.exception.NoExistTripScheduleRegistryException;
import moheng.trip.domain.Trip;
import moheng.trip.domain.repository.TripRepository;
import moheng.trip.exception.NoExistTripException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class TripScheduleService {
    private final MemberRepository memberRepository;
    private final TripScheduleRepository tripScheduleRepository;
    private final TripRepository tripRepository;
    private final TripScheduleRegistryRepository tripScheduleRegistryRepository;

    public TripScheduleService(final MemberRepository memberRepository,
                               final TripScheduleRepository tripScheduleRepository,
                               final TripRepository tripRepository,
                               final TripScheduleRegistryRepository tripScheduleRegistryRepository) {
        this.memberRepository = memberRepository;
        this.tripScheduleRepository = tripScheduleRepository;
        this.tripRepository = tripRepository;
        this.tripScheduleRegistryRepository = tripScheduleRegistryRepository;
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
                true,
                member
        );
        tripScheduleRepository.save(tripSchedule);
    }

    @Transactional
    public void addCurrentTripOnPlannerSchedule(final long tripId, final AddTripOnScheduleRequests addTripOnScheduleRequests) {
        final Trip trip = tripRepository.findById(tripId)
                .orElseThrow(NoExistTripException::new);

        for(final Long scheduleId : addTripOnScheduleRequests.getScheduleIds()) {
            final TripSchedule tripSchedule = tripScheduleRepository.findById(scheduleId)
                    .orElseThrow(NoExistTripScheduleException::new);

            tripScheduleRegistryRepository.save(new TripScheduleRegistry(trip, tripSchedule));
        }
    }

    public FindTripsOnSchedule findTripsOnSchedule(final long scheduleId) {
        final TripSchedule tripSchedule = tripScheduleRepository.findById(scheduleId)
                .orElseThrow(NoExistTripScheduleException::new);

        return new FindTripsOnSchedule(tripSchedule, tripRepository.findTripsByScheduleId(scheduleId));
    }

    @Transactional
    public void updateTripOrdersOnSchedule(final long scheduleId, final UpdateTripOrdersRequest updateTripOrdersRequest) {
        final TripSchedule tripSchedule = tripScheduleRepository.findById(scheduleId)
                .orElseThrow(NoExistTripScheduleException::new);

        tripScheduleRegistryRepository.deleteAllByTripScheduleId(scheduleId);
        addAllTripOnPlannerSchedule(tripSchedule, updateTripOrdersRequest.getTripIds());
    }

    private void addAllTripOnPlannerSchedule(final TripSchedule tripSchedule, final List<Long> tripIds) {
        final List<TripScheduleRegistry> tripScheduleRegistries = new ArrayList<>();

        for (final Long tripId : tripIds) {
            final Trip trip = tripRepository.findById(tripId)
                    .orElseThrow(NoExistTripException::new);
            tripScheduleRegistries.add(new TripScheduleRegistry(trip, tripSchedule));
        }
        tripScheduleRegistryRepository.saveAll(tripScheduleRegistries);
    }

    @Transactional
    public void deleteTripOnSchedule(final long scheduleId, final long tripId) {
        if(!tripScheduleRegistryRepository.existsByTripIdAndTripScheduleId(tripId, scheduleId)) {
            throw new NoExistTripScheduleRegistryException("존재하지 않는 일정 여행지입니다.");
        }
        tripScheduleRegistryRepository.deleteByTripIdAndTripScheduleId(tripId, scheduleId);
    }
}
