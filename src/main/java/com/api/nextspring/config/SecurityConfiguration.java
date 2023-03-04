package com.api.nextspring.config;

import com.api.nextspring.enums.ApplicationUserPermissions;
import com.api.nextspring.security.JwtAuthenticationEntryPoint;
import com.api.nextspring.security.JwtAuthenticationFilter;
import com.api.nextspring.security.JwtTokenProvider;
import com.api.nextspring.security.JwtUsernameAndPasswordFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.api.nextspring.enums.ApplicationUserRoles.ADMIN;

/**
 * security config
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtTokenProvider jwtTokenProvider;

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * authentication manager bean
	 *
	 * @param configuration configuration
	 * @return {@link AuthenticationManager}
	 * @throws Exception java.lang. exception
	 * @see AuthenticationManager
	 */
	@Bean
	public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

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
		// http basic authentication
		http
				.csrf().disable() // disable csrf
				.authorizeHttpRequests((authorize) ->
						authorize
								.requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll() // allow all GET requests
								.requestMatchers("/api/v1/admin/**").hasRole(
										ADMIN.name()
								)
								.requestMatchers("/api/v1/admin/**").hasAnyAuthority(
										ApplicationUserPermissions.USER_READ.getPermission(),
										ADMIN.name()
								)
								.requestMatchers("/api/v1/auth/**").permitAll()
								.anyRequest().authenticated() // all other requests must be authenticated
				)
				.exceptionHandling(
						exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				) // handles exception when user is not authenticated
				.sessionManagement(
						session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				); // disable session creation

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilter(new JwtUsernameAndPasswordFilter(jwtTokenProvider));

		return http.build();
	}
}
