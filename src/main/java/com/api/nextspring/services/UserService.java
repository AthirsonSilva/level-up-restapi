package com.api.nextspring.services;

import java.util.Set;

import com.api.nextspring.entity.RoleEntity;
import com.api.nextspring.payload.UserDto;
import com.api.nextspring.payload.optionals.OptionalUserDto;

public interface UserService {
	public UserDto getCurrentUser(String token);

	public UserDto updateCurrentUser(String token, OptionalUserDto request);

	public void deleteCurrentUser(String token);

	public Set<RoleEntity> getUserRole(String token);
}
