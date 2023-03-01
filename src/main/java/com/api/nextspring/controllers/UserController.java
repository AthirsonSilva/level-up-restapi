package com.api.nextspring.controllers;

import com.api.nextspring.payload.OptionalUserDto;
import com.api.nextspring.payload.Response;
import com.api.nextspring.payload.UserDto;
import com.api.nextspring.services.UserServices;
import com.api.nextspring.utils.GenerateHashMapResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	private final UserServices userServices;
	private final GenerateHashMapResponse<String, Object> generator;

	public UserController(UserServices userServices, GenerateHashMapResponse<String, Object> generator) {
		this.userServices = userServices;
		this.generator = generator;
	}

	@GetMapping
	public ResponseEntity<Response<String, Object>> getCurrentUser(@RequestHeader Map<String, String> headers) {
		String token = getJwtFromRequest(headers);

		UserDto currentUser = userServices.getCurrentUser(token);

		Response<String, Object> response = generator.generateHashMapResponse("Logged in successfully!", currentUser);

		return ResponseEntity.ok(response);
	}

	@PatchMapping
	public ResponseEntity<Response<String, Object>> updateCurrentUser(@RequestHeader Map<String, String> headers, @RequestBody OptionalUserDto request) {
		String token = getJwtFromRequest(headers);

		UserDto updatedCurrentUser = userServices.updateCurrentUser(token, request);

		Response<String, Object> response = generator.generateHashMapResponse("Current user updated successfully!", updatedCurrentUser);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping
	public ResponseEntity<HashMap<String, String>> helloDelete(@RequestHeader Map<String, String> headers) {
		String token = getJwtFromRequest(headers);

		userServices.deleteCurrentUser(token);

		HashMap<String, String> response = new HashMap<>();

		response.put("message", "Current user deleted successfully!");

		return ResponseEntity.ok(response);
	}

	private String getJwtFromRequest(@RequestHeader Map<String, String> headers) {
		String bearerToken = headers.get("authorization");

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}

		return bearerToken;
	}
}
