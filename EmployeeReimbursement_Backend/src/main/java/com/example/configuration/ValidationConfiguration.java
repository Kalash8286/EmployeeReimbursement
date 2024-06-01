package com.example.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * ValidationConfiguration class has Configuration for MessageSource
 * 
 * @author Kalash Vishwakarma
 * 
 * */
@Configuration
@PropertySource({"classpath:error.properties"})
public class ValidationConfiguration {

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource=new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames(new String[] {"classpath:error"});
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
	
}