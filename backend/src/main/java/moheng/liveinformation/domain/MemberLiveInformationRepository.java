package moheng.liveinformation.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberLiveInformationRepository extends JpaRepository<MemberLiveInformation, Long> {
    List<MemberLiveInformation> findByMemberId(final Long memberId);

    @Transactional
    void deleteByMemberId(Long memberId);

    @Query("SELECT m.liveInformation FROM MemberLiveInformation m WHERE m.member.id = :memberId")
    List<LiveInformation> findLiveInformationsByMemberId(@Param("memberId") final Long memberId);
}
