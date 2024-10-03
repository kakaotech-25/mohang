package moheng.global.replication;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DataSourceConfig {
    private static final String source = "source";
    private static final String replica = "replica";

    @Bean
    @Qualifier(source)
    @ConfigurationProperties(prefix = "spring.datasource.source")
    public DataSource sourceDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean @Qualifier(replica)
    @ConfigurationProperties(prefix = "spring.datasource.replica")
    public DataSource replicaDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public DataSource routingDataSource(
            @Qualifier(source) DataSource sourceDataSource,
            @Qualifier(replica) DataSource replicaDataSource) {
        RoutingDataSource routingDataSource = new RoutingDataSource();

        HashMap<Object, Object> targetDataSourceMap = new HashMap<>();

        targetDataSourceMap.put("master", sourceDataSource);
        targetDataSourceMap.put("slave", replicaDataSource);

        routingDataSource.setTargetDataSources(targetDataSourceMap);
        routingDataSource.setDefaultTargetDataSource(sourceDataSource);

        return routingDataSource;
    }

    @Bean
    @Primary
    public DataSource dataSource(){
        DataSource determinedDataSource = routingDataSource(sourceDataSource(), replicaDataSource());
        return new LazyConnectionDataSourceProxy(determinedDataSource);
    }
}
