package com.api.nextspring.controllers;

import com.api.nextspring.payload.DeveloperDto;
import com.api.nextspring.payload.Response;
import com.api.nextspring.payload.optionals.OptionalDeveloperDto;
import com.api.nextspring.services.DeveloperServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/developers")
@RequiredArgsConstructor
public class DeveloperController {
	private final DeveloperServices developerServices;

	@PostMapping
	public ResponseEntity<Response<String, Object>> create(@Validated @RequestBody DeveloperDto request) {
		DeveloperDto creator = developerServices.create(request);

		Response<String, Object> response = new Response<>("Developer created successfully", creator);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<Response<String, Object>> getAll() {
		List<DeveloperDto> creator = developerServices.getAll();

		Response<String, Object> response = new Response<>("All developers fetched successfully", creator);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Response<String, Object>> get(@PathVariable UUID id) {
		DeveloperDto creator = developerServices.getDeveloperByUUID(id);

		Response<String, Object> response = new Response<>("Developer fetched successfully", creator);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Response<String, Object>> update(@PathVariable UUID id, @Validated @RequestBody OptionalDeveloperDto request) {
		DeveloperDto creator = developerServices.updateDeveloper(id, request);

		Response<String, Object> response = new Response<>("Developer updated successfully", creator);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<HashMap<String, Object>> delete(@PathVariable UUID id) {
		developerServices.delete(id);

		HashMap<String, Object> response = new HashMap<>();

		response.put("message", "Developer deleted successfully");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
