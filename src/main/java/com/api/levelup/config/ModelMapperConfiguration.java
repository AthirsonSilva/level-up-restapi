package com.api.levelup.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

/**
 * This class provides configuration for the ModelMapper bean.
 * 
 * @see ModelMapper
 * @param modelMapper The ModelMapper bean.
 * @return a new instance of the ModelMapper class.
 * 
 * @author Athirson Silva
 */
@Configuration
public class ModelMapperConfiguration {

	/**
	 * This method creates and returns a ModelMapper bean.
	 * 
	 * @return ModelMapper bean
	 */
	@Bean
	@Description("This bean configures the ModelMapper")
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
