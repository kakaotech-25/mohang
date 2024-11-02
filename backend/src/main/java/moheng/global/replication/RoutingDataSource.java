package moheng.global.replication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class RoutingDataSource extends AbstractRoutingDataSource {
    private final Logger log = LoggerFactory.getLogger(RoutingDataSource.class);
    private final FindReplicaDataSource findReplicaDataSource = new FindReplicaDataSource();

    @Override
    protected Object determineCurrentLookupKey() {
        final String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        final boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        if(isReadOnly) {
            final String REPLICA_SOURCE_NAME = findReplicaDataSource.getRouteReplicaSource();
            log.info(currentTransactionName + " Transaction:" + REPLICA_SOURCE_NAME + " 서버로 요청합니다.");
            return findReplicaDataSource.getRouteReplicaSource();
        }
        log.info(currentTransactionName + " Transaction:" + "SOURCE 서버로 요청합니다.");
        return "SOURCE";
    }
}