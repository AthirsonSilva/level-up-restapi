package com.api.nextspring.controllers;

import com.api.nextspring.payload.*;
import com.api.nextspring.services.AuthenticationServices;
import com.api.nextspring.utils.GenerateHashMapResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoint for login, register and logout")
public class AuthenticationController {

	private final AuthenticationServices authenticationServices;
	private final GenerateHashMapResponse<String, Object> generateHashMapResponse;

	@PostMapping("/login")
	@Operation(summary = "User login endpoint")
	public ResponseEntity<Response<String, Object>> userLogin(@Validated @RequestBody LoginDto request) {
		String authenticationToken = authenticationServices.userLogin(request);

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Logged in successfully!", authenticationToken);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/register")
	@Operation(summary = "User register endpoint")
	public ResponseEntity<Response<String, Object>> userRegister(@Validated @RequestBody RegisterDto request) {
		UserDto authenticationUserObject = authenticationServices.userRegister(request);

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Registered in successfully!", authenticationUserObject);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/logout")
	@Operation(summary = "User logout endpoint")
	public ResponseEntity<Response<String, Object>> userLogout() {
		authenticationServices.userLogout();

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Logged out successfully!", "Go to login page!");

		return ResponseEntity.ok(response);
	}
}
