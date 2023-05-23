package com.api.nextspring.controllers;

import com.api.nextspring.payload.GenreDto;
import com.api.nextspring.payload.Response;
import com.api.nextspring.payload.optionals.OptionalGenreDto;
import com.api.nextspring.services.GenreServices;
import com.api.nextspring.utils.GenerateHashMapResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Genre", description = "Genre endpoint for getting, creating, updating and deleting genres")
public class GenreController {
    private final GenreServices genreServices;
    private final GenerateHashMapResponse<Object, Object> generateHashMapResponse;

    @GetMapping
    @Operation(summary = "Get all genres endpoint")
    public ResponseEntity<Response<String, Object>> getAllGenres() {
        List<GenreDto> genreList = genreServices.getAllGenres();

        Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Genres fetched successfully!", genreList);

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a genre by id endpoint")
    public ResponseEntity<Response<String, Object>> getGenreById(@PathVariable UUID id) {
        GenreDto genreDto = genreServices.getGenreById(id);

        Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Genre fetched successfully!", genreDto);

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/search")
    @Operation(summary = "Search a genre by name endpoint")
    public ResponseEntity<Response<String, Object>> getGenreByName(@RequestParam String query) {
        List<GenreDto> genreDto = genreServices.searchGenre(query);

        Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Genre fetched successfully!", genreDto);

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping
    @Operation(summary = "Create a genre endpoint")
    public ResponseEntity<Response<String, Object>> createGenre(@Validated @RequestBody GenreDto request) {
        GenreDto genreDto = genreServices.createGenre(request);

        Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Genre created successfully!", genreDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a genre by id endpoint")
    public ResponseEntity<HashMap<String, String>> deleteGenre(@PathVariable UUID id) {
        genreServices.deleteGenre(id);

        HashMap<String, String> response = new HashMap<>();

        response.put("message", "Genre deleted successfully!");

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a genre by id endpoint")
    public ResponseEntity<Response<String, Object>> updateGenre(@Validated @RequestBody OptionalGenreDto request, @PathVariable UUID id) {
        GenreDto genreDto = genreServices.updateGenre(id, request);

        Response<String, Object> response = generateHashMapResponse.generateHashMapResponse("Genre updated successfully!", genreDto);

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
