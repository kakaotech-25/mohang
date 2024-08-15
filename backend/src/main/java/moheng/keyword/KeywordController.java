package moheng.keyword;


import moheng.auth.dto.Accessor;
import moheng.auth.presentation.authentication.Authentication;
import moheng.keyword.dto.KeywordCreateRequest;
import moheng.keyword.dto.TripsByKeyWordsRequest;
import moheng.keyword.service.KeywordService;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.dto.TripKeywordCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/keyword")
@RestController
public class KeywordController {
    private final KeywordService keywordService;

    public KeywordController(final KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    @GetMapping("/travel/model")
    public ResponseEntity<FindTripsResponse> recommendTripsByKeywords(@Authentication final Accessor accessor,
                                                         @RequestBody final TripsByKeyWordsRequest request) {
        return ResponseEntity.ok(keywordService.findRecommendTripsByKeywords(request));
    }

    @PostMapping
    public ResponseEntity<Void> createKeyword(@RequestBody final KeywordCreateRequest request) {
        keywordService.createKeyword(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/trip")
    public ResponseEntity<Void> createTripKeyword(@RequestBody final TripKeywordCreateRequest request) {
        keywordService.createTripKeyword(request);
    }
}
