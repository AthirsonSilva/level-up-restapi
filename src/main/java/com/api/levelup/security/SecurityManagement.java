package com.api.levelup.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.api.levelup.enums.UserRoles;

import lombok.RequiredArgsConstructor;

/**
 * This class provides configuration for the security. It also provides
 * configuration for the authentication manager bean and the security filter
 * chain.
 * 
 * @see AuthenticationManager
 * @see SecurityFilterChain
 * @see JwtAuthenticationEntryPoint
 * @see JwtAuthenticationFilter
 * @see JwtTokenProvider
 * @see JwtUsernameAndPasswordFilter
 * 
 * @param jwtAuthenticationEntryPoint The JwtAuthenticationEntryPoint bean.
 * @param jwtAuthenticationFilter     The JwtAuthenticationFilter bean.
 * @param jwtTokenProvider            The JwtTokenProvider bean.
 * 
 * @return a new instance of the SecurityConfiguration class.
 * 
 * @throws Exception java.lang.exception
 * 
 * @author Athirson Silva
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityManagement {

	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtTokenProvider jwtTokenProvider;

	/**
	 * security filter chain
	 *
	 * @param http http
	 * @return {@link SecurityFilterChain}
	 * @throws Exception java.lang. exception
	 * @see SecurityFilterChain
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable());
		http.authorizeHttpRequests(
				request -> request
						.requestMatchers(HttpMethod.GET).permitAll()
						.requestMatchers("/api/v1/auth/**").permitAll()
						.requestMatchers("/api/v1/admin/**").hasAnyRole(UserRoles.ADMIN.name())
						.anyRequest().authenticated());
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilter(new JwtUsernameAndPasswordFilter(jwtTokenProvider));
		http.exceptionHandling(handling -> handling.authenticationEntryPoint(jwtAuthenticationEntryPoint));

		return http.build();
	}
}
