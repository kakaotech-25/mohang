package moheng.liveinfo.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveInformationRepository extends JpaRepository<LiveInformation, Long> {
    LiveInformation findByName(String liveId);
}
