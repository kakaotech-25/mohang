package moheng.applicationrunner;

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

@Order(7)
@Component
public class TripKeywordApplicationRunner implements ApplicationRunner {
    private final TripKeywordRepository tripKeywordRepository;
    private final TripRepository tripRepository;
    private final KeywordRepository keywordRepository;

    public TripKeywordApplicationRunner(final TripKeywordRepository tripKeywordRepository,
                                        final TripRepository tripRepository,
                                        final KeywordRepository keywordRepository) {
        this.tripKeywordRepository = tripKeywordRepository;
        this.tripRepository = tripRepository;
        this.keywordRepository = keywordRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
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

        Keyword keyword1 = keywordRepository.findById(1L).get();
        Keyword keyword2 = keywordRepository.findById(2L).get();
        Keyword keyword3 = keywordRepository.findById(3L).get();
        Keyword keyword4 = keywordRepository.findById(4L).get();
        Keyword keyword5 = keywordRepository.findById(5L).get();

        tripKeywordRepository.save(new TripKeyword(trip1, keyword1));
        tripKeywordRepository.save(new TripKeyword(trip1, keyword2));
        tripKeywordRepository.save(new TripKeyword(trip2, keyword3));
        tripKeywordRepository.save(new TripKeyword(trip2, keyword4));
        tripKeywordRepository.save(new TripKeyword(trip3, keyword5));
        tripKeywordRepository.save(new TripKeyword(trip3, keyword1));
        tripKeywordRepository.save(new TripKeyword(trip4, keyword2));
        tripKeywordRepository.save(new TripKeyword(trip4, keyword3));
        tripKeywordRepository.save(new TripKeyword(trip5, keyword4));
        tripKeywordRepository.save(new TripKeyword(trip5, keyword5));
        tripKeywordRepository.save(new TripKeyword(trip6, keyword1));
        tripKeywordRepository.save(new TripKeyword(trip6, keyword2));
        tripKeywordRepository.save(new TripKeyword(trip7, keyword3));
        tripKeywordRepository.save(new TripKeyword(trip7, keyword4));
        tripKeywordRepository.save(new TripKeyword(trip8, keyword5));
        tripKeywordRepository.save(new TripKeyword(trip8, keyword1));
        tripKeywordRepository.save(new TripKeyword(trip9, keyword2));
        tripKeywordRepository.save(new TripKeyword(trip9, keyword3));
        tripKeywordRepository.save(new TripKeyword(trip10, keyword4));
        tripKeywordRepository.save(new TripKeyword(trip10, keyword5));

    }
}
