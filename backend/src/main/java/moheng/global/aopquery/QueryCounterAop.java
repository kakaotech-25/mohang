package moheng.global.aopquery;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class QueryCounterAop {
    private static final int MAX_SAFE_QUERY_COUNT = 5;
    private static final Logger logger = LoggerFactory.getLogger(QueryCounterAop.class);
    private final ThreadLocal<QueryLog> currentLoggingForm;

    public QueryCounterAop() {
        this.currentLoggingForm = new ThreadLocal<>();
    }

    @Around("execution( * javax.sql.DataSource.getConnection())")
    public Object captureConnection(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Object connection = joinPoint.proceed();

        return new ConnectionProxyHandler(connection, getCurrentLog()).getProxy();
    }

    @After("within(@org.springframework.web.bind.annotation.RestController *)")

    public void loggingAfterLogicFinish() {
        final QueryLog loggingForm = getCurrentLog();
        final ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (isInRequestScope(attributes)) {
            final HttpServletRequest request = attributes.getRequest();

            loggingForm.setApiMethod(request.getMethod());
            loggingForm.setApiUrl(request.getRequestURI());
        }

        final QueryLog currentQueryLog = getCurrentLog();

        if(currentQueryLog.getQueryCounts() > MAX_SAFE_QUERY_COUNT) {
            logger.warn("count: {}", currentQueryLog.getQueryCounts());
            logger.warn("url: {}", currentQueryLog.getApiUrl());
            logger.warn("time: {}", currentQueryLog.getQueryTime());
            logger.warn("method: {}", currentQueryLog.getApiMethod());
        }

        currentLoggingForm.remove();
    }

    private QueryLog getCurrentLog() {
        if (currentLoggingForm.get() == null) {
            currentLoggingForm.set(new QueryLog());
        }
        return currentLoggingForm.get();
    }

    private boolean isInRequestScope(final ServletRequestAttributes attributes) {
        return attributes != null;
    }
}

