package moheng.applicationrunner.dev;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.KeywordRepository;
import moheng.keyword.domain.TripKeyword;
import moheng.keyword.domain.TripKeywordRepository;
import moheng.trip.domain.Trip;
import moheng.trip.domain.TripRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(3)
@Component
public class KeywordApplicationRunner implements ApplicationRunner {

    private final KeywordRepository keywordRepository;
    private final TripKeywordRepository tripKeywordRepository;
    private final TripRepository tripRepository;

    public KeywordApplicationRunner(final KeywordRepository keywordRepository, final TripKeywordRepository tripKeywordRepository, final TripRepository tripRepository) {
        this.keywordRepository = keywordRepository;
        this.tripKeywordRepository = tripKeywordRepository;
        this.tripRepository = tripRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        Keyword keyword1 = keywordRepository.save(new Keyword("키워드1"));
        Keyword keyword2 = keywordRepository.save(new Keyword("키워드2"));
        Keyword keyword3 = keywordRepository.save(new Keyword("키워드3"));
        Keyword keyword4 = keywordRepository.save(new Keyword("키워드4"));
        Keyword keyword5 = keywordRepository.save(new Keyword("키워드5"));

        Trip trip1 = tripRepository.findById(1L).get();
        Trip trip2 = tripRepository.findById(2L).get();
        Trip trip3 = tripRepository.findById(3L).get();
        Trip trip4 = tripRepository.findById(4L).get();
        Trip trip5 = tripRepository.findById(5L).get();
        Trip trip6 = tripRepository.findById(6L).get();
        Trip trip7 = tripRepository.findById(7L).get();
        Trip trip8 = tripRepository.findById(8L).get();
        Trip trip9 = tripRepository.findById(9L).get();
        Trip trip10 = tripRepository.findById(10L).get();

        tripKeywordRepository.save(new TripKeyword(trip1, keyword1));
        tripKeywordRepository.save(new TripKeyword(trip1, keyword2));
        tripKeywordRepository.save(new TripKeyword(trip1, keyword3));
        tripKeywordRepository.save(new TripKeyword(trip1, keyword4));
        tripKeywordRepository.save(new TripKeyword(trip2, keyword1));
        tripKeywordRepository.save(new TripKeyword(trip2, keyword2));
        tripKeywordRepository.save(new TripKeyword(trip3, keyword2));
        tripKeywordRepository.save(new TripKeyword(trip3, keyword3));
        tripKeywordRepository.save(new TripKeyword(trip4, keyword4));
        tripKeywordRepository.save(new TripKeyword(trip5, keyword5));
        tripKeywordRepository.save(new TripKeyword(trip6, keyword1));
        tripKeywordRepository.save(new TripKeyword(trip7, keyword2));
        tripKeywordRepository.save(new TripKeyword(trip8, keyword3));
        tripKeywordRepository.save(new TripKeyword(trip9, keyword4));
        tripKeywordRepository.save(new TripKeyword(trip10, keyword5));
    }
}
