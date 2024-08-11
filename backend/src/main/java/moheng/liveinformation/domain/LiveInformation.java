package moheng.liveinformation.domain;

import jakarta.persistence.*;
import moheng.global.entity.BaseEntity;
import moheng.liveinformation.exception.LiveInfoNameException;

@Table(name = "live_information")
@Entity
public class LiveInformation extends BaseEntity {
    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    protected LiveInformation() {
    }

    public LiveInformation(Long id, String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    public LiveInformation(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if(name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new LiveInfoNameException("생활정보 이름의 길이는 최소 1자 이상, 최대 100자 이하만 허용됩니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
