package com.api.nextspring.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
public class ModelMapperConfiguration {

	@Bean
	@Description("This bean configures the ModelMapper")
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
