package moheng.planner.domain;

import moheng.planner.exception.InvalidDateSequenceException;

import java.time.LocalDate;

public class Period {
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Period(final LocalDate startDate, final LocalDate endDate) {
        validateDateSequence();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void validateDateSequence() {
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
}
