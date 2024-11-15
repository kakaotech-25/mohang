package moheng.planner.domain;

import jakarta.persistence.*;
import moheng.global.entity.BaseEntity;
import moheng.member.domain.Member;
import moheng.planner.exception.InvalidTripScheduleDateException;
import moheng.planner.exception.InvalidTripScheduleDescriptionException;
import moheng.planner.exception.InvalidTripScheduleNameException;

import java.time.LocalDate;

@Table(name = "trip_schedule")
@Entity
public class TripSchedule extends BaseEntity {
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_DESCRIPTION_LENGTH = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "description")
    private String description;

    @Column(name = "is_private", nullable = false)
    private boolean isPrivate;

    protected TripSchedule() {
    }

    public TripSchedule(final Long id, final String name, final LocalDate startDate, final LocalDate endDate, final Member member) {
        validateName(name);
        validateDate(startDate, endDate);
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.member = member;
    }

    public TripSchedule(final String name, final LocalDate startDate, final LocalDate endDate, final boolean isPrivate, final Member member) {
        validateName(name);
        validateDate(startDate, endDate);
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPrivate = isPrivate;
        this.member = member;
    }

    public TripSchedule(final String name, final String description, final LocalDate startDate, final LocalDate endDate, final boolean isPrivate, final Member member) {
        validateName(name);
        validateDate(startDate, endDate);
        validateDescription(description);
        this.description = description;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPrivate = isPrivate;
        this.member = member;
    }

    private void validateName(final String name) {
        if(name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new InvalidTripScheduleNameException(String.format("일정 이름은 %d자 이상 %d이하여야 합니다.",  MIN_NAME_LENGTH, MAX_NAME_LENGTH));
        }
    }

    private void validateDate(final LocalDate startDate, final LocalDate endDate) {
        if(startDate.isAfter(endDate)) {
            throw new InvalidTripScheduleDateException("플래너 일정의 시작날짜가 종료날짜보다 늦을 수 없습니다.");
        }
    }

    private void validateDescription(final String description) {
        if(MAX_DESCRIPTION_LENGTH < description.length()) {
            throw new InvalidTripScheduleDescriptionException(String.format("일정 세부 설명은 최대 %d자를 허용합니다.", MAX_DESCRIPTION_LENGTH));
        }
    }

    public boolean isNameChanged(final String inputName) {
        return !name.equals(inputName);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public String getDescription() {
        return description;
    }
}
