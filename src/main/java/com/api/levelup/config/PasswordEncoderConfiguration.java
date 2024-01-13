package com.api.levelup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for setting up the password encoder bean using
 * BCryptPasswordEncoder.
 * 
 * @see BCryptPasswordEncoder
 * @see PasswordEncoder
 * 
 * @param passwordEncoder() Returns a new instance of BCryptPasswordEncoder.
 * @return a new instance of BCryptPasswordEncoder
 * 
 * @author Athirson Silva
 */
@Configuration
public class PasswordEncoderConfiguration {

	/**
	 * Returns a new instance of BCryptPasswordEncoder.
	 * 
	 * @return a new instance of BCryptPasswordEncoder
	 */
	@Bean
	static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
