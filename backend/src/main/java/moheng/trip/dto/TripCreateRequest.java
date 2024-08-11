package moheng.trip.dto;

public class TripCreateRequest {
    private String name;
    private String placeName;
    private Long contentId;
    private String description;
    private String tripImageUrl;

    private TripCreateRequest() {
    }

    public TripCreateRequest(String name, String placeName, Long contentId, String description, String tripImageUrl) {
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
