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
    private static final String master = "master";
    private static final String slave = "slave";

    @Bean
    @Qualifier(master)
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean @Qualifier(slave)
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public DataSource routingDataSource(
            @Qualifier(master) DataSource masterDataSource,
            @Qualifier(slave) DataSource slaveDataSource){
        RoutingDataSource routingDataSource = new RoutingDataSource();

        HashMap<Object, Object> targetDataSourceMap = new HashMap<>();

        targetDataSourceMap.put("master", masterDataSource);
        targetDataSourceMap.put("slave", slaveDataSource);

        routingDataSource.setTargetDataSources(targetDataSourceMap);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

    @Bean
    @Primary
    public DataSource dataSource(){
        DataSource determinedDataSource = routingDataSource(masterDataSource(), slaveDataSource());
        return new LazyConnectionDataSourceProxy(determinedDataSource);
    }
}
