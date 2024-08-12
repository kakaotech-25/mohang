package moheng.trip.domain;

import jakarta.persistence.*;
import moheng.global.annotation.Generated;
import moheng.global.entity.BaseEntity;
import moheng.trip.exception.InvalidTripDescriptionException;
import moheng.trip.exception.InvalidTripNameException;

@Table(name = "trip")
@Entity
public class Trip extends BaseEntity {
    private static final long MAX_NAME_LENGTH = 100;
    private static final long MIN_NAME_LENGTH = 2;

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

    @Column(name = "visited_count")
    private Long visitedCount;

    protected Trip() {
    }

    public Trip(String name, String placeName, Long contentId, String description, String tripImageUrl) {
        validateName(name);
        validatePlaceName(placeName);
        validateDescription(description);
        this.name = name;
        this.placeName = placeName;
        this.contentId = contentId;
        this.description = description;
        this.tripImageUrl = tripImageUrl;
        this.visitedCount = 0L;
    }

    public Trip(String name, String placeName, Long contentId, String description, String tripImageUrl, Long visitedCount) {
        validateName(name);
        validatePlaceName(placeName);
        validateDescription(description);
        this.name = name;
        this.placeName = placeName;
        this.contentId = contentId;
        this.description = description;
        this.tripImageUrl = tripImageUrl;
        this.visitedCount = visitedCount;
    }

    void validateName(String name) {
        if(name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH ) {
            throw new InvalidTripNameException("여행지 이름의 길이는 100자 이하, 2자 이상만 허용됩니다.");
        }
    }

    void validatePlaceName(String placeName) {
        if(placeName.length() < MIN_NAME_LENGTH || placeName.length() > MAX_NAME_LENGTH ) {
            throw new InvalidTripNameException("여행지 장소명의 길이는 100자 이하, 2자 이상만 허용됩니다.");
        }
    }

    void validateDescription(String description) {
        if(description.isEmpty() || description == null) {
            throw new InvalidTripDescriptionException("여행지 설명은 비어있을 수 없습니다.");
        }
        if(description.length() < MIN_NAME_LENGTH) {
            throw new InvalidTripDescriptionException("여행지 설명의 길이는 2자 이상만 허용됩니다.");
        }
    }

    public String getName() {
        return name;
    }

    public Long getContentId() {
        return contentId;
    }

    @Generated
    public Long getId() {
        return id;
    }

    @Generated
    public String getDescription() {
        return description;
    }

    public String getPlaceName() {
        return placeName;
    }

    @Generated
    public String getTripImageUrl() {
        return tripImageUrl;
    }

    @Generated
    public Long getVisitedCount() {
        return visitedCount;
    }
}
