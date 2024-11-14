package moheng.liveinformation.dto.request;

import jakarta.validation.constraints.NotEmpty;

public class LiveInformationCreateRequest {

    @NotEmpty(message = "생활정보 이름은 공백일 수 없습니다.")
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
