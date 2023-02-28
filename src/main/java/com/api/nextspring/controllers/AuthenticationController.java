package com.api.nextspring.controllers;

import com.api.nextspring.payload.LoginDto;
import com.api.nextspring.payload.RegisterDto;
import com.api.nextspring.payload.Response;
import com.api.nextspring.payload.UserDto;
import com.api.nextspring.services.AuthenticationServices;
import com.api.nextspring.utils.GenerateHashMapResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	private final AuthenticationServices authenticationServices;
	private final GenerateHashMapResponse<Object, Object> generateHashMapResponse;

	public AuthenticationController(AuthenticationServices authenticationServices, GenerateHashMapResponse<Object, Object> generateHashMapResponse) {
		this.authenticationServices = authenticationServices;
		this.generateHashMapResponse = generateHashMapResponse;
	}

	@PostMapping("/login")
	public ResponseEntity<Response<Object, Object>> login(@Validated @RequestBody LoginDto request) {
		String authentication = authenticationServices.login(request);

		Response<Object, Object> response = generateHashMapResponse.generateHashMapResponse("Logged in successfully!", authentication);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/register")
	public ResponseEntity<Response<Object, Object>> register(@Validated @RequestBody RegisterDto request) {
		UserDto authentication = authenticationServices.register(request);

		Response<Object, Object> response = generateHashMapResponse.generateHashMapResponse("Registered in successfully!", authentication);

		return ResponseEntity.ok(response);
	}
}
