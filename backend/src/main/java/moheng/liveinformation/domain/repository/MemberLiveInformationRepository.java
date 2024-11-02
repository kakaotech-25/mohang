package moheng.liveinformation.domain.repository;

import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.MemberLiveInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberLiveInformationRepository extends JpaRepository<MemberLiveInformation, Long> {
    List<MemberLiveInformation> findByMemberId(final Long memberId);

    @Query("SELECT mli FROM MemberLiveInformation mli JOIN FETCH mli.liveInformation WHERE mli.member.id = :memberId")
    List<MemberLiveInformation> findByMemberIdWithLiveInformation(@Param("memberId") Long memberId);

    @Transactional
    void deleteByMemberId(Long memberId);

    @Query("SELECT m.liveInformation FROM MemberLiveInformation m WHERE m.member.id = :memberId")
    List<LiveInformation> findLiveInformationsByMemberId(@Param("memberId") final Long memberId);
}
