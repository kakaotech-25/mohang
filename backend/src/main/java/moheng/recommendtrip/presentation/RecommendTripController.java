package moheng.recommendtrip.presentation;

import moheng.member.dto.request.SignUpInterestTripsRequest;
import moheng.recommendtrip.application.RecommendTripService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecommendTripController {
    private final RecommendTripService recommendTripService;

    public RecommendTripController(final RecommendTripService recommendTripService) {
        this.recommendTripService = recommendTripService;
    }
}
