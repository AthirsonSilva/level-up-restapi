package com.api.nextspring.security;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.nextspring.exceptions.RestApiException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * This class is responsible for filtering incoming requests and authenticating
 * them using JWT token.
 * It extends OncePerRequestFilter class and overrides doFilterInternal method
 * to implement the authentication logic.
 * It also has a private method getTokenFromRequestHeaders to extract JWT token
 * from the request headers.
 * 
 * @see OncePerRequestFilter
 * @see JwtTokenProvider
 * @see UserDetailsService
 * @author Athirson Silva
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtTokenProvider jwtTokenProvider;
	private final UserDetailsService userDetailsService;

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
	@SuppressWarnings("unchecked")
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		// get jwt token from request
		String jwtToken = getTokenFromRequestHeaders(request);

		// validate jwt token
		if (StringUtils.hasText(jwtToken) && jwtTokenProvider.validateJwtToken(jwtToken)) {
			// get username from jwt token
			String username = jwtTokenProvider.getUsernameFromJwtToken(jwtToken);

			try {
				// parse jwt token and get claims
				Jws<Claims> jwtClaims = Jwts.parserBuilder()
						.setSigningKey(jwtTokenProvider.key())
						.build()
						.parseClaimsJws(jwtToken);

				// get claims from jwt token
				Claims claims = jwtClaims.getBody();

				// get username from claims
				String usernameFromClaims = claims.getSubject();

				// get user details from username
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				// get authorities from claims
				List<Map<String, String>> authorities = claims.get("authorities", List.class);

				// create list of authorities from user details
				List<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities
						.stream()
						.map(
								authority -> new SimpleGrantedAuthority(authority.get("authority")))
						.toList();

				// create authentication token
				Authentication authentication = new UsernamePasswordAuthenticationToken(
						userDetails,
						usernameFromClaims,
						simpleGrantedAuthorities);

				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (JwtException e) {
				throw new RestApiException(HttpStatus.BAD_REQUEST, "Invalid JWT token" + jwtToken + " \n" + e.getMessage());
			}
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
