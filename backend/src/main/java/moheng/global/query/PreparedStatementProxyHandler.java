package moheng.global.query;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.List;

public class PreparedStatementProxyHandler implements MethodInterceptor {
    private static final List<String> JDBC_QUERY_METHOD = List.of("executeQuery", "execute", "executeUpdate");

    private final QueryLog queryLog;

    public PreparedStatementProxyHandler(final QueryLog queryLog) {
        this.queryLog = queryLog;
    }

    @Nullable
    @Override
    public Object invoke(@Nonnull final MethodInvocation invocation) throws Throwable {

        final Method method = invocation.getMethod();

        if (JDBC_QUERY_METHOD.contains(method.getName())) {
            final long startTime = System.currentTimeMillis();
            final Object result = invocation.proceed();
            final long endTime = System.currentTimeMillis();

            queryLog.addQueryTime(endTime - startTime);
            queryLog.queryCountUp();

            return result;
        }

        return invocation.proceed();
    }
}
