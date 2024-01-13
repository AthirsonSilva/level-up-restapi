package com.api.levelup.security;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * This class extends the UsernamePasswordAuthenticationFilter class and
 * overrides the successfulAuthentication method
 * to generate a JWT token and add it to the response headers and cookies.
 * 
 * @see UsernamePasswordAuthenticationFilter
 * @see JwtTokenProvider
 * 
 * @author Athirson Silva
 */
@RequiredArgsConstructor
@Log4j2
public class JwtUsernameAndPasswordFilter extends UsernamePasswordAuthenticationFilter {
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void successfulAuthentication(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain,
			@NonNull Authentication authenticationResult) throws ServletException, IOException {
		String token = Jwts
				.builder()
				.setSubject(authenticationResult.getName())
				.claim("authorities", authenticationResult.getAuthorities())
				.setIssuedAt(Date.valueOf(LocalDate.now()))
				.setExpiration(Date.valueOf(LocalDate.now().plusWeeks(2)))
				.signWith(jwtTokenProvider.key())
				.compact();

		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("Access-Control-Expose-Headers", "Authorization");
		response.addCookie(new Cookie("Authorization", "Bearer " + token));

		log.info("Token generated to expire in: {}", Date.valueOf(LocalDate.now().plusWeeks(2)));

		super.successfulAuthentication(request, response, filterChain, authenticationResult);
	}
}
