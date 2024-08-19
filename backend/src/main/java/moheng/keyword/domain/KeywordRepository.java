package moheng.keyword.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    @Query("SELECT k.name FROM Keyword k WHERE k.id IN :keywordIds")
    List<String> findNamesByIds(@Param("keywordIds") List<Long> keywordIds);

    @Query("SELECT k FROM Keyword k WHERE k.id = :id")
    Optional<Keyword> findKeywordById(@Param("id") Long id);

    @Query("SELECT MIN(k.id) FROM Keyword k")
    Long findMinKeywordId();

    @Query("SELECT MAX(k.id) FROM Keyword k")
    Long findMaxKeywordId();

}
