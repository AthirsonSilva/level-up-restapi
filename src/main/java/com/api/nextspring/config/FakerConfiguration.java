package com.api.nextspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import com.github.javafaker.Faker;

@Configuration
public class FakerConfiguration {

	@Bean
	@Description("This bean configures Faker dependency")
	Faker faker() {
		return new Faker();
	}
}
