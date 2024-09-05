package moheng.keyword.domain;

import jakarta.persistence.*;
import moheng.global.entity.BaseEntity;
import moheng.keyword.exception.KeywordNameLengthException;

@Table(name = "keyword")
@Entity
public class Keyword extends BaseEntity {
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MIN_NAME_LENGTH = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    protected Keyword() {
    }

    public Keyword(final String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(final String name) {
        if(name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new KeywordNameLengthException("키워드 이름의 길이는 최소 1자 이상, 최대 100자 이하만 허용합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
