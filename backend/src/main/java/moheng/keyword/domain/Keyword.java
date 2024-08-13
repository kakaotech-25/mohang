package moheng.keyword.domain;

import jakarta.persistence.*;
import moheng.global.entity.BaseEntity;

@Table(name = "keyword")
@Entity
public class Keyword extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    protected Keyword() {
    }

    public Keyword(final String name) {
        this.name = name;
    }
}
