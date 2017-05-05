package com.robocubs4205.cubscout;

import com.robocubs4205.cubscout.rest.StringToGameConverterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.jdo.PersistenceManagerFactory;

@Configuration
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebConfig extends WebMvcConfigurerAdapter {
    private final PersistenceManagerFactory pmf;

    @Autowired
    public WebConfig(PersistenceManagerFactory pmf){
        this.pmf = pmf;
    }
    @Override
    public void addFormatters(FormatterRegistry registry){
        registry.addConverterFactory(new StringToGameConverterFactory(pmf));
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}
