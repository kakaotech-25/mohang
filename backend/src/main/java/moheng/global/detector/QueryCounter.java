package moheng.global.detector;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class QueryCounter {
    private int count;

    public void increaseCount() {
        count++;
    }

    public int getCount() {
        return count;
    }
}
