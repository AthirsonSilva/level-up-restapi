package com.api.nextspring.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain
	) throws ServletException, IOException {
		final String authorizationHeader = request.getHeader("Authorization");
		final String jwtToken;
		final String userEmail;

		// If the request doesn't have the Authorization header, then it's not a request to the API
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);

			return;
		}

		// Extract the token from the Authorization header and the username from the token
		jwtToken = authorizationHeader.substring(7);
		userEmail = jwtService.extractUsername(jwtToken);

		// If the username is not null and the user is not authenticated, then authenticate the user
		if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// Load the user details from the database
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

			// Verify if the token is valid
			if (jwtService.isTokenValid(jwtToken, userDetails)) {
				// If the token is valid, then create a new authentication with the user details
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities()
				);

				// Set the details of the request
				authenticationToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request)
				);

				// Sets the authentication token in the context
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}

		// Continue the filter chain
		filterChain.doFilter(request, response);
	}
}
