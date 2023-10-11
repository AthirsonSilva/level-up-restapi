package com.api.nextspring.services.impl;

import java.util.Set;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.nextspring.dto.ChangePasswordDto;
import com.api.nextspring.dto.EmailDto;
import com.api.nextspring.dto.UserDto;
import com.api.nextspring.dto.optionals.OptionalUserDto;
import com.api.nextspring.entity.RoleEntity;
import com.api.nextspring.entity.UserEntity;
import com.api.nextspring.exceptions.RestApiException;
import com.api.nextspring.repositories.UserRepository;
import com.api.nextspring.security.JwtTokenProvider;
import com.api.nextspring.services.EmailService;
import com.api.nextspring.services.UserService;
import com.api.nextspring.utils.FileManager;
import com.github.javafaker.Faker;

import lombok.RequiredArgsConstructor;

/**
 * This class implements the UserService interface and provides the
 * implementation for the user-related operations.
 * It uses the UserRepository to interact with the database, JwtTokenProvider to
 * handle authentication, ModelMapper to map
 * entities to DTOs, PasswordEncoder to encode passwords, EmailService to send
 * emails, Faker to generate fake data,
 * excelExporter to export data to Excel files, and EntityfileManager to handle
 * file
 * operations.
 * 
 * @author Athirson Silva
 * @implNote This class implements the UserService interface and provides the
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	// Dependencies
	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;
	private final Faker faker;
	private final FileManager fileManager;

	/**
	 * Returns the current user based on the provided token.
	 * 
	 * @param token The JWT token.
	 * @return The current user.
	 */
	public UserDto getCurrentUser(String token) {
		String authentication = jwtTokenProvider.getUsernameFromJwtToken(token);

		UserEntity currentUser = userRepository.findByEmail(authentication)
				.orElseThrow(() -> new RuntimeException("User not found"));

		return modelMapper.map(currentUser, UserDto.class);
	}

	/**
	 * Updates the current user based on the provided token and request.
	 * 
	 * @param token   The JWT token.
	 * @param request The user update request.
	 * @return The updated user.
	 */
	public UserDto updateCurrentUser(String token, OptionalUserDto request) {
		UserEntity currentUser = getUserEntityFromToken(token);

		if (request.getName() != null)
			currentUser.setName(request.getName());

		if (request.getEmail() != null)
			currentUser.setEmail(request.getEmail());

		if (request.getPassword() != null)
			currentUser.setPassword(passwordEncoder.encode(request.getPassword()));

		userRepository.save(currentUser);

		return modelMapper.map(currentUser, UserDto.class);
	}

	/**
	 * Deletes the current user based on the provided token.
	 * 
	 * @param token The JWT token.
	 */
	public void deleteCurrentUser(String token) {
		UserEntity currentUser = getUserEntityFromToken(token);

		userRepository.delete(currentUser);
	}

	/**
	 * Returns the roles of the current user based on the provided token.
	 * 
	 * @param token The JWT token.
	 * @return The roles of the current user.
	 */
	public Set<RoleEntity> getUserRole(String token) {
		String authentication = jwtTokenProvider.getUsernameFromJwtToken(token);

		UserEntity currentUser = userRepository.findByEmail(authentication)
				.orElseThrow(() -> new RuntimeException("User not found"));

		return currentUser.getRoles();
	}

	/**
	 * Downloads the photo of a user based on the provided ID.
	 * 
	 * @param id The ID of the user.
	 * @return The input stream resource of the photo.
	 */
	@Override
	public InputStreamResource downloadPhotoByUser(UUID id) {
		return null;
	}

	/**
	 * Uploads a photo for the current user based on the provided token and file.
	 * 
	 * @param token The JWT token.
	 * @param file  The photo file.
	 * @return The updated user.
	 */
	@Override
	public UserDto uploadPhoto(String token, MultipartFile file) {
		String authentication = jwtTokenProvider.getUsernameFromJwtToken(token);

		UserEntity entity = userRepository.findByEmail(authentication)
				.orElseThrow(() -> new RuntimeException("User not found"));

		String filePath = fileManager.savePhoto(entity.getId(), file);

		entity.setPhotoPath(filePath);

		UserEntity save = userRepository.save(entity);

		return modelMapper.map(save, UserDto.class);
	}

	/**
	 * Changes the password of the current user based on the provided token and
	 * request.
	 * 
	 * @param token   The JWT token.
	 * @param request The change password request.
	 * @return The updated user.
	 */
	@Override
	public UserDto changeCurrentUserPassword(String token, ChangePasswordDto request) {
		UserEntity currentUser = getUserEntityFromToken(token);

		if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword()))
			throw new RestApiException(HttpStatus.BAD_REQUEST, "Current password is incorrect!");

		currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));

		userRepository.save(currentUser);

		return modelMapper.map(currentUser, UserDto.class);
	}

	/**
	 * Returns the user entity based on the provided token.
	 * 
	 * @param token The JWT token.
	 * @return The user entity.
	 */
	private UserEntity getUserEntityFromToken(String token) {
		String authentication = jwtTokenProvider.getUsernameFromJwtToken(token);

		return userRepository.findByEmail(authentication)
				.orElseThrow(() -> new RuntimeException("User not found"));
	}

	/**
	 * Resets the password of the current user based on the provided token.
	 * Sends an email to the user with the new password.
	 * 
	 * @param token The JWT token.
	 */
	@Override
	public void resetCurrentUserPassword(String token) {
		UserEntity currentUser = getUserEntityFromToken(token);

		String newPass = faker.internet().password(8, 20);

		currentUser.setPassword(passwordEncoder.encode(newPass));

		userRepository.save(currentUser);

		EmailDto email = EmailDto
				.builder()
				.username(currentUser.getName())
				.destination("athirson.silva@proton.me")
				.content("Your new password is: " + newPass)
				.subject("Password reset")
				.sender("athirsonarceus@gmail.com")
				.build();

		emailService.sendPasswordResetEmail(email);
	}

}
