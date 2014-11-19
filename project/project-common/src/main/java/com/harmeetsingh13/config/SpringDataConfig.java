/**
 * 
 */
package com.harmeetsingh13.config;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author Harmeet Singh(Taara)
 *
 */

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages={"com.harmeetsingh13.repo"})
@PropertySource(value="classpath:properties/db.properties")
public class SpringDataConfig {

	@Autowired
	private Environment environment; 
	
	@Bean
	public DataSource dataSource() {
		try{
			ComboPooledDataSource dataSource = new ComboPooledDataSource();
			dataSource.setJdbcUrl(environment.getProperty("db.url"));
			dataSource.setDriverClass(environment.getProperty("db.driver.class"));
			dataSource.setUser(environment.getProperty("db.username"));
			dataSource.setPassword(environment.getProperty("db.password"));
			dataSource.setAcquireIncrement(5);
			dataSource.setIdleConnectionTestPeriod(60);
			dataSource.setMaxPoolSize(100);
			dataSource.setMaxStatements(50);
			dataSource.setMinPoolSize(10);
			return dataSource; 
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
	private JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setShowSql(true);
		jpaVendorAdapter.setDatabase(Database.MYSQL);
		return jpaVendorAdapter;
	}
	
	@Bean
	@Autowired
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setPackagesToScan(environment.getProperty("package.scan"));
		factoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
		return factoryBean;
	}
	
	@Bean
	@Autowired
	public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean factoryBean) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(factoryBean.getObject());
		return transactionManager;
	}
}
