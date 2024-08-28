package moheng.global.detector;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);
    private static final String QUERY_COUNT_LOG = "METHOD: {}, URL: {}, STATUS_CODE: {}, QUERY_COUNT: {}";
    private static final String QUERY_COUNT_WARN_LOG = "쿼리가 {}번 이상 실행되었습니다.";
    private static final int WARN_QUERY_COUNT= 8;
    private final QueryCounter queryCounter;

    public LoggingInterceptor(final QueryCounter queryCounter) {
        this.queryCounter = queryCounter;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
                                Object handler, Exception ex) {
        final int queryCount = queryCounter.getCount();
        logger.info(QUERY_COUNT_LOG, request.getMethod(), request.getRequestURI(), response.getStatus(), queryCount);

        if (queryCount >= WARN_QUERY_COUNT) {
            logger.warn(QUERY_COUNT_WARN_LOG, WARN_QUERY_COUNT);
        }
    }
}