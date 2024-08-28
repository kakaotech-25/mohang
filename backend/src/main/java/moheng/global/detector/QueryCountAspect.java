package moheng.global.detector;

import moheng.global.error.ControllerAdvice;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;

@Aspect
@Component
public class QueryCountAspect {
    private static final Logger logger = LoggerFactory.getLogger(QueryCountAspect.class);
    private final QueryCounter queryCounter;

    public QueryCountAspect(final QueryCounter queryCounter) {
        this.queryCounter = queryCounter;
    }

    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Object getConnection(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object connection = proceedingJoinPoint.proceed();
        return Proxy.newProxyInstance(
                connection.getClass().getClassLoader(),
                connection.getClass().getInterfaces(),
                new ConnectionProxyHandler(connection, queryCounter)
        );
    }
}
