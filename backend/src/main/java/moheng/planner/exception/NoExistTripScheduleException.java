package moheng.planner.exception;

import moheng.member.exception.NoExistMemberException;

public class NoExistTripScheduleException extends RuntimeException {
    public NoExistTripScheduleException(final String message) {
        super(message);
    }

    public NoExistTripScheduleException() {
        super("존재하지 않는 여행 일정입니다.");
    }
}
