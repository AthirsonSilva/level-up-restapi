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
	private final GenerateHashMapResponse<String, Object> generateHashMapResponse;

	public AuthenticationController(AuthenticationServices authenticationServices, GenerateHashMapResponse<String, Object> generateHashMapResponse) {
		this.authenticationServices = authenticationServices;
		this.generateHashMapResponse = generateHashMapResponse;
	}

	@PostMapping("/login")
	public ResponseEntity<Response<String, Object>> login(@Validated @RequestBody LoginDto request) {
		String authenticationToken = authenticationServices.login(request);

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Logged in successfully!", authenticationToken);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/register")
	public ResponseEntity<Response<String, Object>> register(@Validated @RequestBody RegisterDto request) {
		UserDto authenticationUserObject = authenticationServices.register(request);

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Registered in successfully!", authenticationUserObject);

		return ResponseEntity.ok(response);
	}
}
