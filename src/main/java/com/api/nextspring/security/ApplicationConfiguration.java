package com.api.nextspring.security;

import com.api.nextspring.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {
	private final UserRepository userRepository;

	/**
	 * @return the user details service that will be used to retrieve the user details
	 */
	@Bean
	public UserDetailsService userDetailsService() {
		return username -> (UserDetails) userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with given email not found"));
	}

	/**
	 * @return the authentication provider that will be used to authenticate the user credentials
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());

		return authenticationProvider;
	}

	/**
	 * @param configuration - AuthenticationConfiguration is a class that is used to create an AuthenticationManager
	 * @return - AuthenticationManager is a class that is used to authenticate a user
	 * @throws Exception - if an error occurs
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	/**
	 * @return the password encoder that will be used to encode the password
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
