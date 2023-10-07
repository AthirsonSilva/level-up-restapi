package com.api.nextspring.services;

import java.util.Set;

import com.api.nextspring.dto.UserDto;
import com.api.nextspring.dto.optionals.OptionalUserDto;
import com.api.nextspring.entity.RoleEntity;

import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
	public UserDto getCurrentUser(String token);

	public UserDto updateCurrentUser(String token, OptionalUserDto request);

	public void deleteCurrentUser(String token);

	public Set<RoleEntity> getUserRole(String token);

	public void exportToExcel(HttpServletResponse response);
}
