package com.api.nextspring.services;

import com.api.nextspring.dto.LoginDto;
import com.api.nextspring.dto.RegisterDto;
import com.api.nextspring.dto.UserDto;

public interface AuthenticationService {
	public UserDto register(RegisterDto request);

	public String login(LoginDto request);

	public void logout();

	public void activateAccount(String token);
}
