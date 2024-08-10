package moheng.trip.domain;

import jakarta.persistence.*;
import moheng.global.entity.BaseEntity;

@Table(name = "trip")
@Entity
public class Trip extends BaseEntity {
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
        this.name = name;
        this.placeName = placeName;
        this.contentId = contentId;
        this.description = description;
        this.tripImageUrl = tripImageUrl;
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
