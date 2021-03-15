package com.movo.insertdata.config.datasource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryRead1",
        transactionManagerRef = "transactionManagerRead1",
        basePackages = { "com.movo.insertdata.dao" }
)
public class Read1DatasourceConfig {

    private JpaProperties jpaProperties;
    @Value("${spring.datasource.read1.jpa.hibernate.dialect}")
    private String read1Dialect;

    public Read1DatasourceConfig(final JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.read1")
    public DataSourceProperties read1DatasourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "read1Datasource")
    public DataSource read1Datasource() {
        return read1DatasourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "entityManagerFactoryRead1")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryRead1(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(read1Datasource())
                .properties(getVendorProperties())
                .packages("com.movo.insertdata.emtity")
                .persistenceUnit("read1PersistenceUnit")
                .build();
    }

    private Map<String, String> getVendorProperties() {
        Map<String, String> map = new HashMap<>();
        map.put("hibernate.dialect", read1Dialect);
        map.put("hibernate.hbm2ddl.auto", "update");
        map.put("hibernate.implicit_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
        map.put("hibernate.physical_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
        jpaProperties.setProperties(map);
        return jpaProperties.getProperties();
    }

    @Bean(name = "transactionManagerRead1")
    PlatformTransactionManager transactionManagerRead1(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryRead1(builder).getObject());
    }
}
