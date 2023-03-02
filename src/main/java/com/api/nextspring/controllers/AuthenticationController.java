package com.api.nextspring.controllers;

import com.api.nextspring.payload.*;
import com.api.nextspring.services.AuthenticationServices;
import com.api.nextspring.utils.GenerateHashMapResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationServices authenticationServices;
	private final GenerateHashMapResponse<String, Object> generateHashMapResponse;

	@PostMapping("/login")
	public ResponseEntity<Response<String, Object>> userLogin(@Validated @RequestBody LoginDto request) {
		String authenticationToken = authenticationServices.userLogin(request);

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Logged in successfully!", authenticationToken);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/register")
	public ResponseEntity<Response<String, Object>> userRegister(@Validated @RequestBody RegisterDto request) {
		UserDto authenticationUserObject = authenticationServices.userRegister(request);

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Registered in successfully!", authenticationUserObject);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/logout")
	public ResponseEntity<Response<String, Object>> userLogout() {
		authenticationServices.userLogout();

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Logged out successfully!", "Go to login page!");

		return ResponseEntity.ok(response);
	}
}
