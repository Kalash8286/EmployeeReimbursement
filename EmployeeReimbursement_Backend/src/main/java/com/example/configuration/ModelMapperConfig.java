package com.example.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

/**
 * ModelMapperConfig class has Configuration for ModelMapper
 * 
 * @author Kalash Vishwakarma
 * 
 * */
@Configuration
public class ModelMapperConfig {
	

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
