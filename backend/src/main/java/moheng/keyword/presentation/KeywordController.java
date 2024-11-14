package moheng.keyword.presentation;


import moheng.auth.dto.Accessor;
import moheng.auth.presentation.authentication.Authentication;
import moheng.keyword.dto.response.FindAllKeywordResponses;
import moheng.keyword.dto.response.FindTripsWithRandomKeywordResponse;
import moheng.keyword.dto.request.KeywordCreateRequest;
import moheng.keyword.dto.request.TripsByKeyWordsRequest;
import moheng.keyword.application.KeywordService;
import moheng.trip.dto.response.FindTripsResponse;
import moheng.trip.dto.request.TripKeywordCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/keyword")
@RestController
public class KeywordController {
    private final KeywordService keywordService;

    public KeywordController(final KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    @PostMapping("/trip/recommend")
    public ResponseEntity<FindTripsResponse> recommendTripsByKeywords(@Authentication final Accessor accessor,
                                                         @RequestBody final TripsByKeyWordsRequest request) {
        return ResponseEntity.ok(keywordService.findRecommendTripsByKeywords(request));
    }

    @GetMapping
    public ResponseEntity<FindAllKeywordResponses> findAllKeywords(@Authentication final Accessor accessor) {
        return ResponseEntity.ok(keywordService.findAllKeywords());
    }

    @PostMapping
    public ResponseEntity<Void> createKeyword(@RequestBody final KeywordCreateRequest request) {
        keywordService.createKeyword(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/trip")
    public ResponseEntity<Void> createTripKeyword(@RequestBody final TripKeywordCreateRequest request) {
        keywordService.createTripKeyword(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/random/trip")
    public ResponseEntity<FindTripsWithRandomKeywordResponse> findTripsByRandomKeyword() {
        return ResponseEntity.ok(keywordService.findRecommendTripsByRandomKeyword());
    }
}
