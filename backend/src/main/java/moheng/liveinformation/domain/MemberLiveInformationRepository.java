package moheng.liveinformation.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberLiveInformationRepository extends JpaRepository<MemberLiveInformation, Long> {
    List<MemberLiveInformation> findByMemberId(Long memberId);
    void deleteByMemberId(Long memberId);
}
