package moheng.global.detector;

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
    private static final Logger logger = LoggerFactory.getLogger(QueryCounterAop.class);

    private final ThreadLocal<QueryLog> currentLoggingForm;

    public QueryCounterAop() {
        this.currentLoggingForm = new ThreadLocal<>();
    }

    @Around("execution( * javax.sql.DataSource.getConnection())")
    public Object captureConnection(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Object connection = joinPoint.proceed();

        return new ConnectionProxyHandler(connection, getCurrentLoggingForm()).getProxy();
    }

    private QueryLog getCurrentLoggingForm() {
        if (currentLoggingForm.get() == null) {
            currentLoggingForm.set(new QueryLog());
        }
        return currentLoggingForm.get();
    }

    @After("within(@org.springframework.stereotype.Service *) || " +
            "within(@org.springframework.stereotype.Repository *)")
    public void loggingAfterApiFinish() {
        final QueryLog loggingForm = getCurrentLoggingForm();

        final ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (isInRequestScope(attributes)) {
            final HttpServletRequest request = attributes.getRequest();

            loggingForm.setApiMethod(request.getMethod());
            loggingForm.setApiUrl(request.getRequestURI());
        }

        logger.info("count: {}", getCurrentLoggingForm().getQueryCounts());
        logger.info("url: {}", getCurrentLoggingForm().getApiUrl());
        logger.info("time: {}", getCurrentLoggingForm().getQueryTime());
        logger.info("method: {}", getCurrentLoggingForm().getApiMethod());
        currentLoggingForm.remove();
    }

    private boolean isInRequestScope(final ServletRequestAttributes attributes) {
        return attributes != null;
    }
}

