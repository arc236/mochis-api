package com.ace.mochis.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.ace.mochis",
        entityManagerFactoryRef = "aiceEntityManagerFactory",
        transactionManagerRef = "aiceTransactionManager")
public class AiceServiceDomainConfig {

    private static final String PERSISTENCE_UNIT = "aice";

    @Bean(name="aiceFlyway", initMethod = "migrate")
    @Qualifier("aiceFlyway")
    Flyway flyway() {
        return Flyway.configure().locations("classpath:/db/migration/aice").dataSource(aiceDataSource()).table("aice_schema_version").load();
    }

    @Bean(name = "aiceEntityManagerFactory")
    @DependsOn("aiceFlyway")
    @PersistenceContext(unitName = "aice")
    public LocalContainerEntityManagerFactoryBean internalEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {

        Map<String,String> props = new HashMap<String,String>();

        props.put("hibernate.hbm2ddl.auto", "none");

        // Migration from hiberate 4 -> hibernate 5
        // props.put("hibernate.ejb.naming_strategy","org.hibernate.cfg.ImprovedNamingStrategy");
        props.put("hibernate.physical_naming_strategy", "com.ace.mochis.util.hibernate.CustomNamingStrategyImpl");
        props.put("hibernate.implicit_naming_strategy", "org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyHbmImpl");
        props.put("hibernate.ejb.interceptor", "com.ace.mochis.util.hibernate.MyCustomHibernateInterceptor");

        return builder
                .dataSource(aiceDataSource())
                .packages("com.ace.mochis")
                .persistenceUnit(PERSISTENCE_UNIT)
                .properties(props)
                .build();
    }

    @Bean(name = "aiceDataSource")
    @ConfigurationProperties(prefix = "aice.datasource")
    protected DataSource aiceDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "aiceTransactionManager")
    public PlatformTransactionManager aiceTransactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setDataSource(aiceDataSource());
        jpaTransactionManager.setPersistenceUnitName(PERSISTENCE_UNIT);
        return jpaTransactionManager;
    }

}
