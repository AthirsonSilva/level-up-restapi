package com.api.levelup.services;

import com.api.levelup.dto.LoginDto;
import com.api.levelup.dto.RegisterDto;
import com.api.levelup.dto.UserDto;

import jakarta.servlet.http.HttpServletRequest;

/**
 * This interface defines the methods for user authentication and registration.
 * 
 * @author Athirson Silva
 * @see com.api.levelup.services.impl.AuthenticationServiceImpl
 */
public interface AuthenticationService {
	/**
	 * Registers a new user with the provided user data.
	 * 
	 * @param request        the request containing the user data to be registered
	 * @param servletRequest the servlet request containing the user IP address and
	 *                       server host url
	 * @return the user data registered
	 */
	public UserDto register(RegisterDto request, HttpServletRequest servletRequest);

	/**
	 * Logs in a user with the provided user data.
	 * 
	 * @param request the request containing the user data to be logged in
	 * @return the JWT token of the user logged in
	 */
	public String login(LoginDto request);

	/**
	 * Clears the JWT token of the user logged in.
	 */
	public void logout();

	/**
	 * Activates the account of the user with the provided token.
	 * 
	 * @param token the token of the user to be activated
	 */
	public void activateAccount(String token);
}
