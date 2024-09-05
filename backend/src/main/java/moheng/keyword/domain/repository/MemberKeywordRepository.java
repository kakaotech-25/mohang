package moheng.keyword.domain.repository;

import moheng.keyword.domain.MemberKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberKeywordRepository extends JpaRepository<MemberKeyword, Long> {
}
