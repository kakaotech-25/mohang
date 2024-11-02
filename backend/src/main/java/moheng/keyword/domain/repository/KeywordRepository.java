package moheng.keyword.domain.repository;

import moheng.keyword.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    @Query("SELECT k.name FROM Keyword k WHERE k.id IN :keywordIds")
    List<String> findNamesByIds(@Param("keywordIds") final List<Long> keywordIds);

    @Query("SELECT k FROM Keyword k WHERE k.id = :id")
    Optional<Keyword> findKeywordById(@Param("id") final Long id);

    @Query("SELECT MIN(k.id) FROM Keyword k")
    Long findMinKeywordId();

    @Query("SELECT MAX(k.id) FROM Keyword k")
    Long findMaxKeywordId();

    Keyword findByName(final String name);

    boolean existsByName(final String name);
}
