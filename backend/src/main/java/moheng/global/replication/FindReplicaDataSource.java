package moheng.global.replication;

import java.util.concurrent.ThreadLocalRandom;

public class FindReplicaDataSource {
    private static final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
    private static final int REPLICA_DATABASE_AMOUNT = 2;

    public String getRouteReplicaSource() {
        int dataSourceIdx = threadLocalRandom.nextInt(1, REPLICA_DATABASE_AMOUNT + 1);
        return "REPLICA" + dataSourceIdx;
    }
}
