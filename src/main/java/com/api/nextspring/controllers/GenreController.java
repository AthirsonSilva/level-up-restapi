package com.api.nextspring.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.nextspring.payload.GenreDto;
import com.api.nextspring.payload.Response;
import com.api.nextspring.payload.optionals.OptionalGenreDto;
import com.api.nextspring.services.impl.GenreServiceImpl;
import com.api.nextspring.utils.GenerateHashMapResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
@Tag(name = "Genre", description = "Genre endpoint for getting, creating, updating and deleting genres")
public class GenreController {
	private final GenreServiceImpl genreServices;
	private final GenerateHashMapResponse<Object, Object> generateHashMapResponse;

	@GetMapping
	@Operation(summary = "Get all genres endpoint")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	public ResponseEntity<Response<String, Object>> getAllGenres() {
		List<GenreDto> genreList = genreServices.findAll();

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Genres fetched successfully!",
				genreList);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get a genre by id endpoint")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	public ResponseEntity<Response<String, Object>> getGenreById(@PathVariable UUID id) {
		GenreDto genreDto = genreServices.findByID(id);

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Genre fetched successfully!",
				genreDto);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/search")
	@Operation(summary = "Search a genre by name endpoint")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	public ResponseEntity<Response<String, Object>> getGenreByName(@RequestParam String query) {
		List<GenreDto> genreDto = genreServices.searchByKeyword(query);

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Genre fetched successfully!",
				genreDto);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping
	@Operation(summary = "Create a genre endpoint")
	@ResponseStatus(HttpStatus.CREATED)
	@SecurityRequirement(name = "JWT Authentication")
	public ResponseEntity<Response<String, Object>> createGenre(@Validated @RequestBody GenreDto request) {
		GenreDto genreDto = genreServices.create(request);

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Genre created successfully!",
				genreDto);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a genre by id endpoint")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	public ResponseEntity<HashMap<String, String>> deleteGenre(@PathVariable UUID id) {
		genreServices.deleteByID(id);

		HashMap<String, String> response = new HashMap<>();

		response.put("message", "Genre deleted successfully!");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Update a genre by id endpoint")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	public ResponseEntity<Response<String, Object>> updateGenre(@Validated @RequestBody OptionalGenreDto request,
			@PathVariable UUID id) {
		GenreDto genreDto = genreServices.updateByID(id, request);

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Genre updated successfully!",
				genreDto);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
