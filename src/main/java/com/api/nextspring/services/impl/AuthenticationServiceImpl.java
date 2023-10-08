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
import com.api.nextspring.entity.RoleEntity;
import com.api.nextspring.entity.UserEntity;
import com.api.nextspring.enums.UserRoles;
import com.api.nextspring.exceptions.RestApiException;
import com.api.nextspring.repositories.RoleRepository;
import com.api.nextspring.repositories.UserRepository;
import com.api.nextspring.security.JwtTokenProvider;
import com.api.nextspring.services.AuthenticationService;
import com.api.nextspring.services.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final RoleRepository roleRepository;
	private final ModelMapper modelMapper;
	private final EmailService emailService;

	public String login(LoginDto request) {
		UserEntity user = userRepository
				.findByEmail(request.getEmail())
				.orElseThrow(() -> new RestApiException(HttpStatus.NOT_FOUND, "User not found"));

		if (!user.isEnabled())
			throw new RestApiException(HttpStatus.BAD_REQUEST, "User account is not activated!");

		if (user.isLocked())
			throw new RestApiException(HttpStatus.BAD_REQUEST, "User account is locked!");

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
			throw new RestApiException(HttpStatus.BAD_REQUEST, "Invalid password!");

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(), request.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return jwtTokenProvider.generateJwtToken(authentication);
	}

	public UserDto register(RegisterDto request) {
		log.info("Registering user: {}", request.toString());

		if (userRepository.existsByEmail(request.getEmail()))
			throw new BadCredentialsException("A user with given email already exists");

		Set<RoleEntity> roles = new HashSet<>();

		RoleEntity role;

		if (request.isAdmin()) {
			role = roleRepository.findByName(UserRoles.ADMIN.name())
					.orElseThrow(() -> new RestApiException(HttpStatus.NOT_FOUND, "Role not found"));
		} else {
			role = roleRepository.findByName(UserRoles.USER.name())
					.orElseThrow(() -> new RestApiException(HttpStatus.NOT_FOUND, "Role not found"));
		}

		roles.add(role);

		UserEntity user = UserEntity
				.builder()
				.name(request.getName())
				.email(request.getEmail())
				.enabled(false)
				.locked(true)
				.password(passwordEncoder.encode(request.getPassword()))
				.roles(roles)
				.build();

		log.info("Saving user: {}", user.toString());

		userRepository.save(user);

		EmailDto emailDto = EmailDto.builder()
				.username(user.getName())
				.destination(user.getEmail())
				.sender("athirsonarceus@gmail.com")
				.subject("Account activation")
				.content("http://localhost:8000/api/v1/auth/confirm-account?token=" + user.getId())
				.build();

		log.info("Sending account confirmation email: {}", emailDto.toString());

		emailService.sendConfirmationEmail(emailDto);

		return modelMapper.map(user, UserDto.class);
	}

	public void logout() {
		SecurityContextHolder.clearContext();
	}

	public void activateAccount(String token) {
		UserEntity user = userRepository
				.findById(UUID.fromString(token))
				.orElseThrow(() -> new RestApiException(HttpStatus.NOT_FOUND, "User with given token was not found!"));

		if (user.isEnabled())
			throw new RestApiException(HttpStatus.BAD_REQUEST, "User account is already activated!");

		user.setEnabled(true);
		user.setLocked(false);

		log.info("Activating user account: {}", user.toString());

		userRepository.save(user);

		log.info("User account activated successfully!");
	}
}