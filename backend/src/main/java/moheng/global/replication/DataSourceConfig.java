package moheng.global.replication;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.HashMap;

@Profile({"dev"})
@Configuration
public class DataSourceConfig {
    private static final String source = "source";
    private static final String replica1 = "replica1";
    private static final String replica2 = "replica2";

    @Bean
    @Qualifier(source)
    @ConfigurationProperties(prefix = "spring.datasource.source")
    public DataSource sourceDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Qualifier(replica1)
    @ConfigurationProperties(prefix = "spring.datasource.replica1")
    public DataSource replica1DataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    @Qualifier(replica2)
    @ConfigurationProperties(prefix = "spring.datasource.replica2")
    public DataSource replica2DataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    public DataSource routingDataSource(
            @Qualifier(source) DataSource sourceDataSource,
            @Qualifier(replica1) DataSource replica1DataSource,
            @Qualifier(replica2) DataSource replica2DataSource) {
        RoutingDataSource routingDataSource = new RoutingDataSource();

        HashMap<Object, Object> dataSources = new HashMap<>();

        dataSources.put("source", sourceDataSource);
        dataSources.put("replica1", replica1DataSource);
        dataSources.put("replica2", replica2DataSource);

        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(sourceDataSource);

        return routingDataSource;
    }

    @Bean
    @Primary
    public DataSource dataSource(){
        DataSource determinedDataSource = routingDataSource(sourceDataSource(), replica1DataSource(), replica2DataSource());
        return new LazyConnectionDataSourceProxy(determinedDataSource);
    }
}