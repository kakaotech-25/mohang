package moheng.auth.dto;

public class Accessor {
    private Long id;

    private Accessor() {
    }

    public Accessor(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
