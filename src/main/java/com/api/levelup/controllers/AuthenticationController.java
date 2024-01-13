package com.api.levelup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.levelup.dto.LoginDto;
import com.api.levelup.dto.RegisterDto;
import com.api.levelup.dto.Response;
import com.api.levelup.dto.UserDto;
import com.api.levelup.services.impl.AuthenticationServiceImpl;
import com.api.levelup.utils.ResponseGenerator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoint for login, register and logout")
@SecurityRequirement(name = "JWT Authentication")
@ApiResponses({
		@ApiResponse(responseCode = "400", description = "Bad Request, the user did not send all required data", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "401", description = "Unauthorized, the user is not logged in or does not have access permition", content = @Content(mediaType = "application/json"))
})
public class AuthenticationController {

	private final AuthenticationServiceImpl authenticationServices;
	private final ResponseGenerator<String, Object> responseGenerator;

	@PostMapping("/login")
	@Operation(summary = "User login endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Response<String, Object>> userLogin(@Validated @RequestBody LoginDto request) {
		String authenticationToken = authenticationServices.login(request);

		Response<String, Object> response = responseGenerator.responseGenerator("Logged in successfully!",
				authenticationToken);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/register")
	@Operation(summary = "User register endpoint")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Response<String, Object>> userRegister(
			@Validated @RequestBody RegisterDto request,
			HttpServletRequest httpServletRequest) {
		UserDto authenticationUserObject = authenticationServices.register(request, httpServletRequest);

		Response<String, Object> response = responseGenerator.responseGenerator(
				"Registered in successfully! An email was sent to your email to confirm your account!",
				authenticationUserObject);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/logout")
	@Operation(summary = "User logout endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Response<String, Object>> userLogout() {
		authenticationServices.logout();

		Response<String, Object> response = responseGenerator.responseGenerator("Logged out successfully!",
				"Go to login page!");

		return ResponseEntity.ok(response);
	}

	@GetMapping("/confirm-account")
	@Operation(summary = "User confirmation and activation account endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Response<String, Object>> confirmUserAccount(String token) {
		authenticationServices.activateAccount(token);

		Response<String, Object> response = responseGenerator.responseGenerator(
				"Account activated successfully!",
				"Go to login page!");

		return ResponseEntity.ok(response);
	}
}
