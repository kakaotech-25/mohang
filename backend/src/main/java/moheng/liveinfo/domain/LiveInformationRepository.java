package moheng.liveinfo.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LiveInformationRepository extends JpaRepository<LiveInformation, Long> {
    Optional<LiveInformation> findByName(String liveId);
}
