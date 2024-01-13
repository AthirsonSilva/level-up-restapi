package com.api.levelup.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JwtAuthenticationEntryPoint is a class that implements
 * AuthenticationEntryPoint interface.
 * It is used to handle authentication errors and send an unauthorized response
 * to the client.
 * 
 * @see AuthenticationEntryPoint
 * @see JwtAuthenticationFilter
 * @author Athirson Silva
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	/**
	 * commence
	 *
	 * @param request       request
	 * @param response      response
	 * @param authException authException
	 * @throws IOException java.io. i o exception
	 */
	@Override
	public void commence(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		// This is invoked when user tries to access a secured REST resource without
		// supplying any credentials
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}
}
