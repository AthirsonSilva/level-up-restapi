package com.api.nextspring.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
	private static final String SECRET_KEY = "F4FDE3E2105C3B2EAE76003E81B68A76F88C59365D07A54A3FBE993525A79743";

	/**
	 * @param userDetails contains the username and the claims to be extracted from the user
	 * @return the JWT token
	 */
	public String generateToken(UserDetails userDetails) {
		return generateTokenClaims(new HashMap<>(), userDetails);
	}

	/**
	 * @param extractClaims the claims to be extracted from the user
	 * @param userDetails the user details to generate the token containing the username and the claims
	 * @return the JWT token
	 */
	public String generateTokenClaims(Map<String, Object> extractClaims, UserDetails userDetails) {
		return Jwts
				.builder()
				.setClaims(extractClaims) // claims extracted from the user
				.setSubject(userDetails.getUsername()) // username extracted from the user
				.setIssuedAt(new Date(System.currentTimeMillis())) // current date and time
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
				.signWith(getSignKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	/**
	 * @param token the JWT token
	 * @param userDetails the user details to validate the token against
	 * @return if the token is valid or not
	 */
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);

		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	/**
	 * @param token the JWT token
	 * @return the username extracted from the token
	 */
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	/**
	 * @param token the JWT token
	 * @return if the token is expired or not by the current date
	 */
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	/**
	 * @param token the JWT token
	 * @return the subject extracted from the token
	 */
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	/**
	 * @param token the JWT token
	 * @param claimsResolver the function to extract the claim
	 * @param <T> the type of the claim
	 * @return the claim extracted from the token
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractTokenClaims(token);

		return claimsResolver.apply(claims);
	}

	/**
	 * @param token the JWT token
	 * @return the claims extracted from the token
	 */
	private Claims extractTokenClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	/**
	 * @return the key used to sign the JWT
	 */
	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

		return Keys.hmacShaKeyFor(keyBytes);
	}
}
