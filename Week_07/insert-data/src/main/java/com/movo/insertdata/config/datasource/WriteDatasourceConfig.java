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
        entityManagerFactoryRef = "entityManagerFactoryWrite",
        transactionManagerRef = "transactionManagerWrite",
        basePackages = { "com.movo.insertdata.dao" }
)
public class WriteDatasourceConfig {

    private JpaProperties jpaProperties;
    @Value("${spring.datasource.write.jpa.hibernate.dialect}")
    private String writeDialect;

    public WriteDatasourceConfig(final JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties;
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.write")
    public DataSourceProperties writeDatasourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "writeDatasource")
    @Primary
    public DataSource writeDatasource() {
        return writeDatasourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "entityManagerFactoryWrite")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryWrite(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(writeDatasource())
                .properties(getVendorProperties())
                .packages("com.movo.insertdata.emtity")
                .persistenceUnit("writePersistenceUnit")
                .build();
    }

    private Map<String, String> getVendorProperties() {
        Map<String, String> map = new HashMap<>();
        map.put("hibernate.dialect", writeDialect);
        map.put("hibernate.hbm2ddl.auto", "update");
        map.put("hibernate.implicit_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
        map.put("hibernate.physical_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
        jpaProperties.setProperties(map);
        return jpaProperties.getProperties();
    }

    @Bean(name = "transactionManagerWrite")
    @Primary
    PlatformTransactionManager transactionManagerWrite(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryWrite(builder).getObject());
    }
}
