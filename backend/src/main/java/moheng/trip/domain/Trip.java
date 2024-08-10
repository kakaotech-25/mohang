package moheng.trip.domain;

import jakarta.persistence.*;
import moheng.global.entity.BaseEntity;
import moheng.trip.exception.InvalidTripNameException;

@Table(name = "trip")
@Entity
public class Trip extends BaseEntity {
    private static final long MAX_NAME_LENGTH = 100;
    private static final long MIN_NAME_LENGTH = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    @Column(name = "content_id", nullable = false)
    private Long contentId;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "trip_image_url", nullable = false)
    private String tripImageUrl;

    protected Trip() {
    }

    public Trip(String name, String placeName, Long contentId, String description, String tripImageUrl) {
        validateName(name);
        validatePlaceName(placeName);
        validateContentId(contentId);
        validateDescription(description);
        validateTripImageUrl(tripImageUrl);
        this.name = name;
        this.placeName = placeName;
        this.contentId = contentId;
        this.description = description;
        this.tripImageUrl = tripImageUrl;
    }

    void validateName(String name) {
        if(name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH ) {
            throw new InvalidTripNameException("여행지 이름의 길이는 100자 이하, 1자 이상만 허용됩니다.");
        }
    }

    void validatePlaceName(String placeName) {

    }

    void validateContentId(Long contentId) {

    }

    void validateDescription(String description) {

    }

    void validateTripImageUrl(String tripImageUrl) {

    }

    public String getName() {
        return name;
    }

    public Long getContentId() {
        return contentId;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getTripImageUrl() {
        return tripImageUrl;
    }
}
