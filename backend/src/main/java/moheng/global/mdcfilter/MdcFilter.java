package moheng.global.mdcfilter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MdcFilter implements Filter {
    private static final String REQUEST_ID = "REQUEST_ID";

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) throws ServletException, IOException {
        setMdc((HttpServletRequest) servletRequest);
        filterChain.doFilter(servletRequest, servletResponse);
        MDC.clear();
    }

    private void setMdc(final HttpServletRequest request) {
        MDC.put("REQUEST_ID", UUID.randomUUID().toString());
        MDC.put("REQUEST_METHOD", request.getMethod());
        MDC.put("REQUEST_URI", request.getRequestURI());
        MDC.put("REQUEST_TIME", LocalDateTime.now().toString());
        MDC.put("REQUEST_IP", request.getRemoteAddr());
    }
}
