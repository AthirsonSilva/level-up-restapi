package com.api.nextspring.controllers;

import com.api.nextspring.entity.RoleEntity;
import com.api.nextspring.services.UserServices;
import com.api.nextspring.utils.GetJwtTokenFromHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
	private final UserServices userServices;
	private final GetJwtTokenFromHeaders getJwtFromRequest;

	@GetMapping("/admin")
	public ResponseEntity<HashMap<String, String>> helloAdmin(@RequestHeader Map<String, String> headers) {
		HashMap<String, String> response = new HashMap<>();
		String token = getJwtFromRequest.execute(headers);

		Set<RoleEntity> userRoles = userServices.getUserRole(token);

		RoleEntity role = userRoles.stream().findFirst().orElseThrow(
				() -> new RuntimeException("User not found")
		);

		response.put("message", "Hello Admin!");
		response.put("role", role.getName());

		return ResponseEntity.ok(response);
	}
}
