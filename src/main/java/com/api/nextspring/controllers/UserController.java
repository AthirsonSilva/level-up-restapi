package com.api.nextspring.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.nextspring.payload.Response;
import com.api.nextspring.payload.UserDto;
import com.api.nextspring.payload.optionals.OptionalUserDto;
import com.api.nextspring.services.UserService;
import com.api.nextspring.utils.GenerateHashMapResponse;
import com.api.nextspring.utils.GetJwtTokenFromHeaders;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User endpoint for getting and updating the current logged in user")
public class UserController {
	private final UserService userServices;
	private final GenerateHashMapResponse<String, Object> generator;
	private final GetJwtTokenFromHeaders getJwtFromRequest;

	@GetMapping
	@Operation(summary = "Get the current logged in user")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	public ResponseEntity<Response<String, Object>> getCurrentUser(@RequestHeader Map<String, String> headers) {
		String token = getJwtFromRequest.execute(headers);

		UserDto currentUser = userServices.getCurrentUser(token);

		Response<String, Object> response = generator.generateHashMapResponse("Logged in successfully!", currentUser);

		return ResponseEntity.ok(response);
	}

	@PatchMapping
	@Operation(summary = "Update the current logged in user")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	public ResponseEntity<Response<String, Object>> updateCurrentUser(@RequestHeader Map<String, String> headers,
			@RequestBody OptionalUserDto request) {
		String token = getJwtFromRequest.execute(headers);

		UserDto updatedCurrentUser = userServices.updateCurrentUser(token, request);

		Response<String, Object> response = generator.generateHashMapResponse("Current user updated successfully!",
				updatedCurrentUser);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping
	@Operation(summary = "Delete the current logged in user")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	public ResponseEntity<HashMap<String, String>> deleteUser(@RequestHeader Map<String, String> headers) {
		String token = getJwtFromRequest.execute(headers);

		userServices.deleteCurrentUser(token);

		HashMap<String, String> response = new HashMap<>();

		response.put("message", "Current user deleted successfully!");

		return ResponseEntity.ok(response);
	}
}
