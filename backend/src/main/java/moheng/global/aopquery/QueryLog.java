package moheng.global.aopquery;

public class QueryLog {
    private String apiUrl;
    private String apiMethod;
    private Long queryCounts = 0L;
    private Long queryTime = 0L;

    public void queryCountUp() {
        queryCounts++;
    }

    public void addQueryTime(final Long queryTime) {
        this.queryTime += queryTime;
    }

    public void setApiUrl(final String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public void setApiMethod(final String apiMethod) {
        this.apiMethod = apiMethod;
    }

    public Long getQueryCounts() {
        return queryCounts;
    }

    public Long getQueryTime() {
        return queryTime;
    }

    public String getApiMethod() {
        return apiMethod;
    }

    public String getApiUrl() {
        return apiUrl;
    }
}
