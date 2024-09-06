package moheng.global.mdc;

import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.LogRecord;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MdcFilter implements Filter {
    private static final String REQUEST_ID = "REQUEST_ID";

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) throws ServletException, IOException {
        MDC.put(REQUEST_ID, UUID.randomUUID().toString());
        filterChain.doFilter(servletRequest, servletResponse);
        MDC.clear();
    }
}
