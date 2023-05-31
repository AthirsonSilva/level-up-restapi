package com.api.nextspring.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.api.nextspring.payload.GameDto;
import com.api.nextspring.payload.Response;
import com.api.nextspring.payload.optionals.OptionalGameDto;
import com.api.nextspring.services.GameService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
@Tag(name = "Game", description = "Game endpoint for creating, getting, updating and deleting games")
public class GameController {
	private final GameService gameServices;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a new game endpoint")
	@SecurityRequirement(name = "JWT Authentication")
	public ResponseEntity<Response<String, GameDto>> createGame(@RequestBody GameDto request) {
		GameDto gameDto = gameServices.create(request);

		Response<String, GameDto> response = new Response<>("Restaurant created successfully!", gameDto);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/search")
	@Operation(summary = "Search a game by name endpoint")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	public ResponseEntity<Response<String, List<GameDto>>> getGames(@RequestParam("query") String query) {
		List<GameDto> gameList = gameServices.searchByKeyword(query);

		if (gameList.size() < 1) {
			Response<String, List<GameDto>> response = new Response<>("No game found with given information's!", gameList);

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		Response<String, List<GameDto>> response = new Response<>("Games found with given information's!", gameList);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping
	@Operation(summary = "Get all games endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Response<String, List<GameDto>>> getAllGames() {
		List<GameDto> gameList = gameServices.findAll();

		Response<String, List<GameDto>> response = new Response<>("All games found!", gameList);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get a game by id endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Response<String, GameDto>> getGameById(@PathVariable("id") UUID id) {
		GameDto gameDto = gameServices.findByID(id);

		Response<String, GameDto> response = new Response<>("Game found with given id!", gameDto);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Update a game by id endpoint")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	public ResponseEntity<Response<String, GameDto>> updateGame(@PathVariable("id") UUID id,
			@RequestBody OptionalGameDto request) {
		GameDto gameDto = gameServices.updateById(id, request);

		Response<String, GameDto> response = new Response<>("Game updated successfully!", gameDto);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a game by id endpoint")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	public ResponseEntity<HashMap<String, String>> deleteGame(@PathVariable("id") UUID id) {
		gameServices.deleteById(id);

		HashMap<String, String> response = new HashMap<>();

		response.put("message", "Game deleted successfully!");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
