package moheng.global.replication;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class RoutingDataSource extends AbstractRoutingDataSource {
    // determineCurrentLookupKey 메소드 : 여러 datasource 중에서 실제로 사용될 DataSource 를 결정하는 역할
    // 현재 트랜잭션의 속성에 따라 targetDataSourceMap 의 조회 Key 를 결정하기위해 AbstractRoutingDataSource 를 상속받아서 determineCurrentLookupKey 를 구현했다.
    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        logger.info("현재 트랜잭션 속성이 ReadOnly인가?:" + isReadOnly);
        return isReadOnly ? "slave" : "master";
    }
}

