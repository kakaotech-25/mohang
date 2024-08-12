package moheng.trip.dto;

import moheng.liveinformation.domain.LiveInformation;
import moheng.trip.domain.Trip;

public class FindTripResponse {
    private String name;
    private String placeName;
    private Long contentId;
    private String tripImageUrl;

    private FindTripResponse() {
    }

    public FindTripResponse(final Trip trip) {
        this.name = trip.getName();
        this.placeName = trip.getPlaceName();
        this.contentId = trip.getContentId();
        this.tripImageUrl = trip.getTripImageUrl();
    }

    public String getName() {
        return name;
    }

    public String getPlaceName() {
        return placeName;
    }

    public Long getContentId() {
        return contentId;
    }

    public String getTripImageUrl() {
        return tripImageUrl;
    }
}
