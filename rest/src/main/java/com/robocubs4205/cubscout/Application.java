package com.robocubs4205.cubscout;

import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jdo.JdoTransactionManager;
import org.springframework.orm.jdo.TransactionAwarePersistenceManagerFactoryProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;


/**
 * Created by trevor on 2/11/17.
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = {RepositoryRestMvcAutoConfiguration.class, OAuth2AutoConfiguration.class,HibernateJpaAutoConfiguration.class})
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    ServletRegistrationBean h2(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new WebServlet());
        bean.addUrlMappings("/console/*");
        return bean;
    }

    @Bean
    TransactionAwarePersistenceManagerFactoryProxy pmf(@Qualifier("true") PersistenceManagerFactory truepmf){
        TransactionAwarePersistenceManagerFactoryProxy pmf = new TransactionAwarePersistenceManagerFactoryProxy();
        pmf.setTargetPersistenceManagerFactory(truepmf);

        return pmf;
    }
    @Bean
    @Qualifier("true")
    PersistenceManagerFactory truepmf(){
        return JDOHelper.getPersistenceManagerFactory("PU");
    }

    @Bean
    JdoTransactionManager jdoTransactionManager(@Qualifier("true") PersistenceManagerFactory pmf){
        return new JdoTransactionManager(pmf);
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}
