package moheng.planner.domain;

import java.time.LocalDate;

public class Period {
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Period(final LocalDate startDate, final LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
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
