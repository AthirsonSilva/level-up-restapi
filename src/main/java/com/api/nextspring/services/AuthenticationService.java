package com.api.nextspring.services;

import com.api.nextspring.payload.LoginDto;
import com.api.nextspring.payload.RegisterDto;
import com.api.nextspring.payload.UserDto;

public interface AuthenticationService {
	public UserDto register(RegisterDto request);

	public String login(LoginDto request);

	public void logout();
}
