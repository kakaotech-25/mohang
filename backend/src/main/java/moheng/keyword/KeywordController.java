package moheng.keyword;


import moheng.auth.dto.Accessor;
import moheng.auth.presentation.authentication.Authentication;
import moheng.keyword.dto.TripsByKeyWordsRequest;
import moheng.keyword.exception.TripRecommendByKeywordRequest;
import moheng.keyword.service.KeywordService;
import moheng.trip.dto.FindTripsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.ok(keywordService.findRecommendTripsByKeywords(accessor.getId(), request));
    }
}
