package com.api.nextspring.services;

import com.api.nextspring.entity.RoleEntity;
import com.api.nextspring.entity.UserEntity;
import com.api.nextspring.enums.RolesOptions;
import com.api.nextspring.exceptions.RestApiException;
import com.api.nextspring.payload.LoginDto;
import com.api.nextspring.payload.RegisterDto;
import com.api.nextspring.payload.UserDto;
import com.api.nextspring.repositories.RoleRepository;
import com.api.nextspring.repositories.UserRepository;
import com.api.nextspring.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServices {
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final RoleRepository roleRepository;
	private final ModelMapper modelMapper;

	public String login(LoginDto request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(), request.getPassword()
				)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return jwtTokenProvider.generateJwtToken(authentication);
	}

	public UserDto register(RegisterDto request) {
		if (userRepository.existsByEmail(request.getEmail()))
			throw new BadCredentialsException("Email already exists");

		if (userRepository.existsByCpf(request.getCpf()))
			throw new BadCredentialsException("CPF already exists");

		request.setCpf(request.getCpf().replace("\\p{Punct}", ""));

		Set<RoleEntity> roles = new HashSet<>();

		RoleEntity role = roleRepository.findByName(RolesOptions.USER)
				.orElseThrow(() -> new RestApiException(HttpStatus.NOT_FOUND, "Role not found"));

		roles.add(role);

		UserEntity user = UserEntity
				.builder()
				.name(request.getName())
				.email(request.getEmail())
				.cpf(request.getCpf())
				.password(passwordEncoder.encode(request.getPassword()))
				.roles(roles)
				.build();

		userRepository.save(user);

		return modelMapper.map(user, UserDto.class);
	}
}