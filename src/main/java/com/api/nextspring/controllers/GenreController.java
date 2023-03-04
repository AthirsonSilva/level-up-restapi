package com.api.nextspring.controllers;

import com.api.nextspring.payload.GenreDto;
import com.api.nextspring.payload.Response;
import com.api.nextspring.payload.optionals.OptionalGenreDto;
import com.api.nextspring.services.GenreServices;
import com.api.nextspring.utils.GenerateHashMapResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
public class GenreController {
	private final GenreServices genreServices;
	private final GenerateHashMapResponse<Object, Object> generateHashMapResponse;

	@GetMapping
	public ResponseEntity<Response<String, Object>> getAllGenres() {
		List<GenreDto> genreList = genreServices.getAllGenres();

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Genres fetched successfully!", genreList);

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Response<String, Object>> getGenreById(@PathVariable UUID id) {
		GenreDto genreDto = genreServices.getGenreById(id);

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Genre fetched successfully!", genreDto);

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@GetMapping("/search")
	public ResponseEntity<Response<String, Object>> getGenreByName(@RequestParam String query) {
		List<GenreDto> genreDto = genreServices.searchGenre(query);

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Genre fetched successfully!", genreDto);

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@PostMapping
	public ResponseEntity<Response<String, Object>> createGenre(@Validated @RequestBody GenreDto request) {
		GenreDto genreDto = genreServices.createGenre(request);

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Genre created successfully!", genreDto);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<HashMap<String, String>> deleteGenre(@PathVariable UUID id) {
		genreServices.deleteGenre(id);

		HashMap<String, String> response = new HashMap<>();

		response.put("message", "Genre deleted successfully!");

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Response<String, Object>> updateGenre(@Validated @RequestBody OptionalGenreDto request, @PathVariable UUID id) {
		GenreDto genreDto = genreServices.updateGenre(id, request);

		Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Genre updated successfully!", genreDto);

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
}
