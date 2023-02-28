package com.api.nextspring.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final UserDetailsService userDetailsService;

	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.userDetailsService = userDetailsService;
	}

	/**
	 * do filter internal
	 *
	 * @param request     request
	 * @param response    response
	 * @param filterChain filterChain
	 * @throws ServletException jakarta.servlet. servlet exception
	 * @throws IOException      java.io. i o exception
	 */
	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain
	) throws ServletException, IOException {
		// get jwt token from request
		String jwtToken = getTokenFromRequestHeaders(request);

		// validate jwt token
		if (StringUtils.hasText(jwtToken) && jwtTokenProvider.validateJwtToken(jwtToken)) {
			// get username from jwt token
			String username = jwtTokenProvider.getUsernameFromJwtToken(jwtToken);

			// get user details from username
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			// create authentication token
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails,
					null,
					userDetails.getAuthorities()
			);

			// set authentication details from request to authentication token
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			// set authentication in security context
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}

		// continue filter chain
		filterChain.doFilter(request, response);
	}

	/**
	 * get token from request headers
	 *
	 * @param request request
	 * @return {@link String}
	 * @see String
	 */
	private String getTokenFromRequestHeaders(HttpServletRequest request) {
		// get authorization header
		String bearerToken = request.getHeader("Authorization");

		// validate authorization header
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			// extract token from authorization header and return it
			return bearerToken.substring(7);
		}

		return null;
	}
}
