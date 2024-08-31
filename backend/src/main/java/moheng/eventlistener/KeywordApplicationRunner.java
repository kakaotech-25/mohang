package moheng.eventlistener;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(3)
@Component
public class KeywordApplicationRunner implements ApplicationRunner {

    private final KeywordRepository keywordRepository;

    public KeywordApplicationRunner(final KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        keywordRepository.save(new Keyword("키워드1"));
        keywordRepository.save(new Keyword("키워드2"));
        keywordRepository.save(new Keyword("키워드3"));
        keywordRepository.save(new Keyword("키워드4"));
        keywordRepository.save(new Keyword("키워드5"));
    }
}
