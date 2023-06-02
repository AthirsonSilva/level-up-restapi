package com.api.nextspring.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.nextspring.payload.LoginDto;
import com.api.nextspring.payload.RegisterDto;
import com.api.nextspring.payload.Response;
import com.api.nextspring.payload.UserDto;
import com.api.nextspring.services.impl.AuthenticationServiceImpl;
import com.api.nextspring.utils.GenerateHashMapResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoint for login, register and logout")
public class AuthenticationController {

	private final AuthenticationServiceImpl authenticationServices;
	private final GenerateHashMapResponse<String, Object> generateHashMapResponse;

	@PostMapping("/login")
	@Operation(summary = "User login endpoint")
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses({
			@ApiResponse(responseCode = "400", description = "Bad Request, the user did not send all required data", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "401", description = "Unauthorized, the user is not logged in or does not have access permition", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Response<String, Object>> userLogin(@Validated @RequestBody LoginDto request) {
		String authenticationToken = authenticationServices.login(request);

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Logged in successfully!",
				authenticationToken);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/register")
	@Operation(summary = "User register endpoint")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponses({
			@ApiResponse(responseCode = "400", description = "Bad Request, the user did not send all required data", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "401", description = "Unauthorized, the user is not logged in or does not have access permition", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Response<String, Object>> userRegister(@Validated @RequestBody RegisterDto request) {
		UserDto authenticationUserObject = authenticationServices.register(request);

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Registered in successfully!",
				authenticationUserObject);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/logout")
	@Operation(summary = "User logout endpoint")
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses({
			@ApiResponse(responseCode = "400", description = "Bad Request, the user did not send all required data", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "401", description = "Unauthorized, the user is not logged in or does not have access permition", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Response<String, Object>> userLogout() {
		authenticationServices.logout();

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Logged out successfully!",
				"Go to login page!");

		return ResponseEntity.ok(response);
	}
}
