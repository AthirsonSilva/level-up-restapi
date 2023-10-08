package com.api.nextspring.services.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.nextspring.dto.EmailDto;
import com.api.nextspring.dto.LoginDto;
import com.api.nextspring.dto.RegisterDto;
import com.api.nextspring.dto.UserDto;
import com.api.nextspring.entity.AddressEntity;
import com.api.nextspring.entity.RoleEntity;
import com.api.nextspring.entity.UserEntity;
import com.api.nextspring.enums.UserRoles;
import com.api.nextspring.exceptions.RestApiException;
import com.api.nextspring.repositories.RoleRepository;
import com.api.nextspring.repositories.UserRepository;
import com.api.nextspring.security.JwtTokenProvider;
import com.api.nextspring.services.AddressService;
import com.api.nextspring.services.AuthenticationService;
import com.api.nextspring.services.EmailService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * This class implements the AuthenticationService interface and provides the
 * implementation for the login, register, logout, and activateAccount methods.
 * It uses an AuthenticationManager, UserRepository, PasswordEncoder,
 * JwtTokenProvider, RoleRepository, ModelMapper, and EmailService to perform
 * authentication and authorization tasks.
 * 
 * @author Athirson SIlva
 * @implNote This class implements the AuthenticationService interface and
 *           provides the
 *           implementation for the login, register, logout, and activateAccount
 *           methods.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

	// Dependencies
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final AddressService addressService;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final RoleRepository roleRepository;
	private final ModelMapper modelMapper;
	private final EmailService emailService;

	/**
	 * Authenticates a user and returns a JWT token.
	 *
	 * @param request The login request containing the user's email and password.
	 * @return A JWT token.
	 * @throws RestApiException If the user is not found, the account is not
	 *                          activated, the account is locked, or the password is
	 *                          invalid.
	 */
	public String login(LoginDto request) throws RestApiException {
		// Find user by email
		UserEntity user = userRepository
				.findByEmail(request.getEmail())
				.orElseThrow(() -> new RestApiException(HttpStatus.NOT_FOUND, "User not found"));

		// Check if user account is enabled
		if (!user.isEnabled())
			throw new RestApiException(HttpStatus.BAD_REQUEST, "User account is not activated!");

		// Check if user account is locked
		if (user.isLocked())
			throw new RestApiException(HttpStatus.BAD_REQUEST, "User account is locked!");

		// Check if password is valid
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
			throw new RestApiException(HttpStatus.BAD_REQUEST, "Invalid password!");

		// Authenticate user
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(), request.getPassword()));

		// Set authentication context
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Generate JWT token
		return jwtTokenProvider.generateJwtToken(authentication);
	}

	/**
	 * Registers a new user.
	 *
	 * @param request        The registration request containing the user's name,
	 *                       email,
	 *                       password, and admin status.
	 * @param servletRequest The servlet request containing the user's IP address
	 *                       and host url
	 * @return The registered user's information.
	 * @throws RestApiException If a user with the given email already exists or the
	 *                          user role is not found.
	 */
	public UserDto register(RegisterDto request, HttpServletRequest servletRequest) throws RestApiException {
		log.info("Registering user: {}", request.toString());

		// Check if user with email already exists
		if (userRepository.existsByEmail(request.getEmail()))
			throw new BadCredentialsException("A user with given email already exists");

		// Checks if password and password confirmation match
		if (!request.getPassword().equals(request.getPasswordConfirmation()))
			throw new BadCredentialsException("Password and password confirmation do not match");

		// Retrieve address by zip code
		AddressEntity retrievedAddress = addressService.getByZipCode(request.getZipCode());

		// Set user role
		Set<RoleEntity> roles = new HashSet<>();
		RoleEntity role = roleRepository.findByName(UserRoles.USER.name())
				.orElseThrow(() -> new RestApiException(HttpStatus.NOT_FOUND, "Role not found"));
		roles.add(role);

		// Create user entity
		UserEntity user = UserEntity
				.builder()
				.name(request.getName())
				.email(request.getEmail())
				.address(retrievedAddress)
				.enabled(false)
				.locked(true)
				.password(passwordEncoder.encode(request.getPassword()))
				.roles(roles)
				.build();

		// Save user entity
		log.info("Saving user: {}", user.toString());
		userRepository.save(user);

		// Get host url from servlet request header
		String hostUrl = servletRequest.getHeader("host");
		log.info("Host url: {}", hostUrl);

		// Create account confirmation link
		String confirmationLink = String.valueOf(hostUrl + "/api/v1/auth/confirm-account?token=" + user.getId());
		log.info("Account confirmation link: {}", confirmationLink);

		// Send account confirmation email
		EmailDto emailDto = EmailDto.builder()
				.username(user.getName())
				.destination(user.getEmail())
				.sender("athirsonarceus@gmail.com")
				.subject("Account activation")
				.content("http://localhost:8080/api/v1/auth/confirm-account?token=" + user.getId())
				.build();

		log.info("Sending account confirmation email: {}", emailDto.toString());

		// Send email asynchronously
		emailService.sendConfirmationEmail(emailDto);

		// Return user DTO
		return modelMapper.map(user, UserDto.class);
	}

	/**
	 * Logs out the current user.
	 */
	public void logout() {
		SecurityContextHolder.clearContext();
	}

	/**
	 * Activates a user account.
	 *
	 * @param token The activation token.
	 * @throws RestApiException If the user with the given token is not found or the
	 *                          account is already activated.
	 */
	public void activateAccount(String token) throws RestApiException {
		// Find user by token
		UserEntity user = userRepository
				.findById(UUID.fromString(token))
				.orElseThrow(() -> new RestApiException(HttpStatus.NOT_FOUND, "User with given token was not found!"));

		log.info("User found: {}", user.toString());

		// Check if account is already activated
		if (user.isEnabled())
			throw new RestApiException(HttpStatus.BAD_REQUEST, "User account is already activated!");

		// Activate account
		log.info("Activating user account: {}", user.toString());
		user.setEnabled(true);
		user.setLocked(false);

		userRepository.save(user);

		log.info("User account activated successfully!");
	}
}