package com.api.nextspring.utils;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;

import com.api.nextspring.exceptions.RestApiException;

/**
 * Utility class for handling JWT tokens.
 * It has a method execute that extracts the JWT token from the authorization
 * header of a request.
 * 
 * @see RestApiException
 * @see HttpStatus
 * @see RequestHeader
 * @see StringUtils
 * 
 * @author Athirson Silva
 */
@Service
public class JwtTokenUtils {

	/**
	 * Extracts the JWT token from the authorization header of a request.
	 * 
	 * @param headers a map containing the headers of the request
	 * @return the bearerToken containing the user JWT token from the authorization
	 *         header
	 * @throws RestApiException containing the error message and the http status
	 *                          code if the user is not logged in
	 */
	public String execute(@RequestHeader Map<String, String> headers) {
		String bearerToken = headers.get("authorization");

		if (bearerToken == null || bearerToken.isEmpty()) {
			throw new RestApiException(HttpStatus.UNAUTHORIZED, "The user is not logged in");
		}

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}

		return bearerToken;
	}
}
