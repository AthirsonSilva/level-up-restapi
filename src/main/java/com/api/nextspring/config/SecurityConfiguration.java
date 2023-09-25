package com.api.nextspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.api.nextspring.enums.UserPermissions;
import com.api.nextspring.enums.UserRoles;
import com.api.nextspring.security.JwtAuthenticationEntryPoint;
import com.api.nextspring.security.JwtAuthenticationFilter;
import com.api.nextspring.security.JwtTokenProvider;
import com.api.nextspring.security.JwtUsernameAndPasswordFilter;

import lombok.RequiredArgsConstructor;

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

	@Bean
	CorsConfigurationSource configurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**",
				new org.springframework.web.cors.CorsConfiguration().applyPermitDefaultValues());

		return source;
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
		http.csrf(csrf -> csrf.disable());
		http.authorizeHttpRequests(
				request -> request
						.requestMatchers(HttpMethod.GET).permitAll()
						.requestMatchers("/api/v1/auth/**").permitAll()
						.requestMatchers("/api/v1/admin/**").hasRole(UserRoles.ADMIN.name())
						.requestMatchers("/api/v1/admin/**").hasAnyAuthority(
								UserPermissions.USER_READ.getPermission(),
								UserRoles.ADMIN.name())
						.anyRequest().authenticated());
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilter(new JwtUsernameAndPasswordFilter(jwtTokenProvider));
		http.exceptionHandling(handling -> handling.authenticationEntryPoint(jwtAuthenticationEntryPoint));

		return http.build();
	}
}
