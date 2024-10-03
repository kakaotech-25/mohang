package moheng.global.replication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class RoutingDataSource extends AbstractRoutingDataSource {
    private final Logger log = LoggerFactory.getLogger(RoutingDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        final String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        final boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        if(isReadOnly) {
            log.info(currentTransactionName + " Transaction:" + "Replica 서버로 요청합니다.");
            return "replica";
        }
        log.info(currentTransactionName + " Transaction:" + "Source 서버로 요청합니다.");
        return "source";
    }
}