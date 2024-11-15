package moheng.planner.domain;

import moheng.planner.exception.InvalidDateSequenceException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Period {
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Period(final LocalDate startDate, final LocalDate endDate) {
        validateDateSequence(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void validateDateSequence(final LocalDate startDate, final LocalDate endDate) {
        if(startDate.isAfter(endDate)) {
            throw new InvalidDateSequenceException("시작날짜는 종료날짜보다 더 이후일 수 없습니다.");
        }
    }

    public boolean isOtherMonthDate() {
        return !isSameYear() || !isSameMonth();
    }

    private boolean isSameYear() {
        return startDate.getYear() == endDate.getYear();
    }

    private boolean isSameMonth() {
        return startDate.getMonthValue() == endDate.getMonthValue();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDateTime getStartDateOfMonth() {
        return startDate.atStartOfDay();
    }

    public LocalDateTime getEndDateOfMonth() {
        return endDate.atTime(LocalTime.MAX);
    }
}
