package moheng.trip.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TripCreateRequest {
    @NotBlank(message = "여행지 이름은 공백일 수 없습니다.")
    private String name;

    @NotBlank(message = "여행지 장소명은 공백일 수 없습니다.")
    private String placeName;

    @NotNull(message = "여행지 contentId는 Null 일 수 없습니다.")
    private Long contentId;

    @NotBlank(message = "여행지 설명은 공백일 수 없습니다.")
    private String description;

    @NotBlank(message = "여행지 이미지 경로는 공백일 수 없습니다.")
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
