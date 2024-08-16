package moheng.trip.domain;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import moheng.config.slice.RepositoryTestConfig;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class MemberTripRepositoryTest extends RepositoryTestConfig {
    @Autowired
    private MemberTripRepository memberTripRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TripRepository tripRepository;

    @DisplayName("멤버의 여행지를 찾는다.")
    @Test
    void 멤버의_여행지를_찾는다() {
        // given
        Member savedMember = memberRepository.save(하온_기존());
        Trip savedTrip = tripRepository.save(new Trip("여행지", "장소명", 1L, "설명", "이미지"));

        Member member = memberRepository.findById(savedMember.getId()).orElseThrow(() -> new IllegalStateException("Member not found"));
        Trip trip = tripRepository.findById(savedTrip.getId()).orElseThrow(() -> new IllegalStateException("Trip not found"));

        memberTripRepository.save(new MemberTrip(member, trip));

        // when, then
        assertDoesNotThrow(() -> memberTripRepository.findByMemberAndTrip(member, trip));
    }

    @DisplayName("멤버의 여행지가 존재하면 참을 리턴한다.")
    @Test
    void 멤버의_여행지가_존재하면_참을_리턴한다() {
        // given
        Member savedMember = memberRepository.save(하온_기존());
        Trip savedTrip = tripRepository.save(new Trip("여행지", "장소명", 1L, "설명", "이미지"));

        Member member = memberRepository.findById(savedMember.getId()).orElseThrow(() -> new IllegalStateException("Member not found"));
        Trip trip = tripRepository.findById(savedTrip.getId()).orElseThrow(() -> new IllegalStateException("Trip not found"));

        memberTripRepository.save(new MemberTrip(member, trip));

        // when, then
        assertTrue(memberTripRepository.existsByMemberAndTrip(member, trip));
    }

}
