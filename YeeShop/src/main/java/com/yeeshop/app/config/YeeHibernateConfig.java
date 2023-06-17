package com.yeeshop.app.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;
import org.springframework.core.env.Environment;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

/**
 * Yee Hibernate Configuration
 * 
 * @author XiaoYi
 */
@Configuration
@PropertySource("classpath:datasource.properties")
public class YeeHibernateConfig {

	/** Inject Spring Core Environment*/
	@Autowired
	Environment env;
	
	/**
	 * getDataSource DataSource
	 * 
	 * @return dataSource
	 * @author XiaoYi
	 */
	@Bean
	public DataSource getDataSource() {
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		
		// set property
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));
        
        // return data source
        return dataSource;
	}
	
	/**
	 * getSessionFactory SessionFactory
	 * 
	 * @param DataSource dataSource
	 * @exception IOException IOException
	 * @return sessionFactory
	 * @author XiaoYi
	 */
    @Bean
    @Autowired
    public SessionFactory getSessionFactory(DataSource dataSource) throws IOException{
        
    	// create Local session bean
    	LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        
    	factoryBean.setPackagesToScan(new String[]{"com.yeeshop.entity"});
        factoryBean.setDataSource(dataSource);
       
        // set property for Hibernate
        Properties props = factoryBean.getHibernateProperties();
       
        props.put("hibernate.dialect", env.getProperty("hb.dialect"));
        props.put("hibernate.show_sql", env.getProperty("hb.show-sql"));
        props.put("current_session_context_class", env.getProperty("hb.session"));
        factoryBean.afterPropertiesSet();
        
        // get Bean Session 
        SessionFactory sessionFactory = factoryBean.getObject();
        
        // return sessionFactory
        return sessionFactory;
    }
    
	/**
	 * getTransactionManager HibernateTransactionManager
	 * 
	 * @param SessionFactory sessionFactory
	 * @return TransactionManager
	 * @author XiaoYi
	 */
    @Bean
	@Autowired
	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
    	
		return new HibernateTransactionManager(sessionFactory);
	}
}
