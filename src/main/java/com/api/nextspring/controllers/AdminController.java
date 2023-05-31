package com.api.nextspring.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.nextspring.entity.RoleEntity;
import com.api.nextspring.services.UserService;
import com.api.nextspring.utils.GetJwtTokenFromHeaders;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin endpoint for getting the current logged in admin")
public class AdminController {
	private final UserService userServices;
	private final GetJwtTokenFromHeaders getJwtFromRequest;

	@GetMapping("/admin")
	@Operation(summary = "Get the current logged in admin")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	public ResponseEntity<HashMap<String, String>> helloAdmin(@RequestHeader Map<String, String> headers) {
		HashMap<String, String> response = new HashMap<>();
		String token = getJwtFromRequest.execute(headers);

		Set<RoleEntity> userRoles = userServices.getUserRole(token);

		RoleEntity role = userRoles.stream().findFirst().orElseThrow(() -> new RuntimeException("User not found"));

		response.put("message", "Hello Admin!");
		response.put("role", role.getName());

		return ResponseEntity.ok(response);
	}
}
