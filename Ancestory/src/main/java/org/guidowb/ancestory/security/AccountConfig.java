package org.guidowb.ancestory.security;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
		entityManagerFactoryRef = "accountEntityManagerFactory", 
		transactionManagerRef = "accountTransactionManager",
		basePackages = { "org.guidowb.ancestory.security" }
		)
public class AccountConfig {

    @Bean(name="accountDataSource")
    public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
             
            dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
            dataSource.setUrl("jdbc:hsqldb:file:db/accounts");
            dataSource.setUsername("sa");
            dataSource.setPassword("");
             
            return dataSource;
    }
     
    @Bean(name="accountEntityManagerFactory")
    public EntityManagerFactory entityManagerFactory() {

      HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
      vendorAdapter.setGenerateDdl(true);

      LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
      factory.setPersistenceUnitName("account");
      factory.setJpaVendorAdapter(vendorAdapter);
      factory.setPackagesToScan(getClass().getPackage().getName());
      factory.setDataSource(dataSource());
      factory.afterPropertiesSet();

      return factory.getObject();
    }
    
    @Bean(name="accountTransactionManager")
    public PlatformTransactionManager transactionManager() {
    	JpaTransactionManager txManager = new JpaTransactionManager();
    	txManager.setEntityManagerFactory(entityManagerFactory());
    	return txManager;
    }
}