package moheng.global.error.dto;

public class ExceptionResponse {
    private final String message;
    private final String description;

    public ExceptionResponse(final String message, final String description) {
        this.message = message;
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
