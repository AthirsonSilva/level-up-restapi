package com.api.nextspring.controllers;

import com.api.nextspring.payload.Response;
import com.api.nextspring.payload.UserDto;
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

	private final GenerateHashMapResponse<String, UserDto> generateHashMapResponse;

	public AuthenticationController(GenerateHashMapResponse<String, UserDto> generateHashMapResponse) {
		this.generateHashMapResponse = generateHashMapResponse;
	}

	@PostMapping
	public ResponseEntity<Response<String, UserDto>> login(@Validated @RequestBody UserDto request) {
		request.setCpf(request.getCpf().replace("\\p{Punct}", ""));

		Response<String, UserDto> response = generateHashMapResponse.generate("User logged in successfully", request);

		return ResponseEntity.ok(response);
	}
}
