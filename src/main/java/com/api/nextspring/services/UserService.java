package com.api.nextspring.services;

import java.util.Set;
import java.util.UUID;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import com.api.nextspring.dto.UserDto;
import com.api.nextspring.dto.optionals.OptionalUserDto;
import com.api.nextspring.entity.RoleEntity;

import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
	public UserDto getCurrentUser(String token);

	public UserDto updateCurrentUser(String token, OptionalUserDto request);

	public void deleteCurrentUser(String token);

	public Set<RoleEntity> getUserRole(String token);

	public UserDto uploadPhoto(UUID id, MultipartFile file);

	public InputStreamResource downloadPhotoByUser(UUID id);

	public void exportToExcel(HttpServletResponse response);
}
