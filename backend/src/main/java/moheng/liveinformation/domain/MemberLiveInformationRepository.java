package moheng.liveinformation.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberLiveInformationRepository extends JpaRepository<MemberLiveInformation, Long> {
    List<MemberLiveInformation> findByMemberId(Long memberId);

    @Transactional
    void deleteByMemberId(Long memberId);
}
