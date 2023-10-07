package com.api.nextspring.services.impl;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.nextspring.dto.UserDto;
import com.api.nextspring.dto.optionals.OptionalUserDto;
import com.api.nextspring.entity.RoleEntity;
import com.api.nextspring.entity.UserEntity;
import com.api.nextspring.enums.EntityOptions;
import com.api.nextspring.exceptions.RestApiException;
import com.api.nextspring.repositories.UserRepository;
import com.api.nextspring.security.JwtTokenProvider;
import com.api.nextspring.services.UserService;
import com.api.nextspring.utils.EntityFileUtils;
import com.api.nextspring.utils.ExcelUtils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final ExcelUtils excelUtils;
	private final EntityFileUtils fileUtils;

	public UserDto getCurrentUser(String token) {
		String authentication = jwtTokenProvider.getUsernameFromJwtToken(token);

		UserEntity currentUser = userRepository.findByEmail(authentication)
				.orElseThrow(() -> new RuntimeException("User not found"));

		return modelMapper.map(currentUser, UserDto.class);
	}

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

	public void deleteCurrentUser(String token) {
		UserEntity currentUser = getUserEntityFromToken(token);

		userRepository.delete(currentUser);
	}

	public Set<RoleEntity> getUserRole(String token) {
		String authentication = jwtTokenProvider.getUsernameFromJwtToken(token);

		UserEntity currentUser = userRepository.findByEmail(authentication)
				.orElseThrow(() -> new RuntimeException("User not found"));

		return currentUser.getRoles();
	}

	private UserEntity getUserEntityFromToken(String token) {
		String authentication = jwtTokenProvider.getUsernameFromJwtToken(token);

		return userRepository.findByEmail(authentication)
				.orElseThrow(() -> new RuntimeException("User not found"));
	}

	@Override
	public void exportToExcel(HttpServletResponse response) {
		List<UserEntity> entityList = userRepository.findAll();

		try {
			excelUtils.export(response, entityList, EntityOptions.USER);
		} catch (IllegalArgumentException e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occurred while exporting data to Excel file: " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occurred while exporting data to Excel file: " + e.getMessage());
		}
	}

	@Override
	public InputStreamResource downloadPhotoByUser(UUID id) {
		UserEntity entity = userRepository
				.findById(id)
				.orElseThrow(
						() -> new RestApiException(
								HttpStatus.NOT_FOUND, "User with given id was not found!"));

		return null;
	}

	@Override
	public UserDto uploadPhoto(UUID id, MultipartFile file) {
		UserEntity entity = userRepository
				.findById(id)
				.orElseThrow(
						() -> new RestApiException(
								HttpStatus.NOT_FOUND, "User with given id was not found!"));

		String filePath = fileUtils.savePhoto(id, file);

		entity.setPhotoPath(filePath);

		UserEntity save = userRepository.save(entity);

		return modelMapper.map(save, UserDto.class);
	}

}
