package com.api.levelup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import com.github.javafaker.Faker;

/**
 * This class provides configuration for the Faker dependency.
 * 
 * @see Faker
 * @param faker The Faker dependency.
 * @return a new instance of the Faker class.
 * 
 * @author Athirson Silva
 */
@Configuration
public class FakerConfiguration {

	/**
	 * This method creates a new instance of the Faker class.
	 * 
	 * @return a new instance of the Faker class.
	 */
	@Bean
	@Description("This bean configures Faker dependency")
	Faker faker() {
		return new Faker();
	}
}
