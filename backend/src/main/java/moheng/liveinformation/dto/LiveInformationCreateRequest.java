package moheng.liveinformation.dto;

public class LiveInformationCreateRequest {
    private String name;

    private LiveInformationCreateRequest() {
    }

    public LiveInformationCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
