package com.api.nextspring.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.api.nextspring.exceptions.RestApiException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * JwtTokenProvider class provides methods to generate, validate and get
 * username from JWT token. It uses the secret key and expiry date from
 * application.properties file.
 * 
 * @see JwtAuthenticationFilter
 * @see JwtAuthenticationEntryPoint
 * @see JwtTokenProvider
 * @see JwtUsernameAndPasswordFilter
 * 
 * @author Athirson Silva
 */
@Component
public class JwtTokenProvider {

	@Value("${app.jwt-secret}")
	private String jwtSecret;

	@Value("${app.jwt-expiration-milliseconds}")
	private long jwtExpirationDate;

	/**
	 * generate jwt token
	 *
	 * @param authentication authentication
	 * @return {@link String}
	 * @see String
	 */
	public String generateJwtToken(Authentication authentication) {
		// get username from authentication
		String username = authentication.getName();

		// get current date
		Date currentDate = new Date();

		// set expiry date
		Date expiryDate = new Date(currentDate.getTime() + jwtExpirationDate);

		// generate and return jwt token with username, current date, expiry date and
		// decoded secret key
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(currentDate)
				.claim("authorities", authentication.getAuthorities())
				.setExpiration(expiryDate)
				.signWith(key())
				.compact();
	}

	/**
	 * key
	 *
	 * @return {@link Key}
	 * @see Key
	 */
	protected Key key() {
		// decode secret key and return it
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	/**
	 * get username from jwt token
	 *
	 * @param token token
	 * @return {@link String}
	 * @see String
	 */
	public String getUsernameFromJwtToken(String token) {
		// parse jwt token and return username
		return Jwts.parserBuilder()
				.setSigningKey(key())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}

	/**
	 * validate jwt token
	 *
	 * @param authToken authToken
	 * @return {@link Boolean}
	 * @see Boolean
	 */
	public boolean validateJwtToken(String authToken) {
		// parse jwt token and return true if it is valid
		try {
			Jwts.parserBuilder()
					.setSigningKey(key())
					.build()
					.parseClaimsJws(authToken);

			return true;
		} catch (MalformedJwtException e) {
			throw new RestApiException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
		} catch (ExpiredJwtException e) {
			throw new RestApiException(HttpStatus.BAD_REQUEST, "Expired JWT token");
		} catch (UnsupportedJwtException e) {
			throw new RestApiException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
		} catch (IllegalArgumentException e) {
			throw new RestApiException(HttpStatus.BAD_REQUEST, "JWT claims string is empty");
		}
	}
}
