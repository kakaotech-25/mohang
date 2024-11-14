package moheng.trip.dto.request;

public class TripCreateRequest {
    private String name;
    private String placeName;
    private Long contentId;
    private String description;
    private String tripImageUrl;

    private TripCreateRequest() {
    }

    public TripCreateRequest(final String name, final String placeName, final Long contentId,
                             final String description, final String tripImageUrl) {
        this.name = name;
        this.placeName = placeName;
        this.contentId = contentId;
        this.description = description;
        this.tripImageUrl = tripImageUrl;
    }

    public String getTripImageUrl() {
        return tripImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getName() {
        return name;
    }

    public Long getContentId() {
        return contentId;
    }
}
