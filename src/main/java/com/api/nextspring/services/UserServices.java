package com.api.nextspring.services;

import com.api.nextspring.entity.RoleEntity;
import com.api.nextspring.entity.UserEntity;
import com.api.nextspring.payload.OptionalUserDto;
import com.api.nextspring.payload.UserDto;
import com.api.nextspring.repositories.UserRepository;
import com.api.nextspring.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServices {
	private final UserRepository userRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;

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

		if (request.getCpf() != null)
			currentUser.setCpf(request.getCpf());

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
}
