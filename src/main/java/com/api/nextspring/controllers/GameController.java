package com.api.nextspring.controllers;

import com.api.nextspring.payload.GameDto;
import com.api.nextspring.payload.Response;
import com.api.nextspring.payload.optionals.OptionalGameDto;
import com.api.nextspring.services.GameServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
@Tag(name = "Game", description = "Game endpoint for creating, getting, updating and deleting games")
public class GameController {
	private final GameServices gameServices;

	@PostMapping
	@Operation(summary = "Create a new game endpoint")
	public ResponseEntity<Response<String, GameDto>> createGame(@RequestBody GameDto request) {
		GameDto gameDto = gameServices.createGame(request);

		Response<String, GameDto> response = new Response<>("Restaurant created successfully!", gameDto);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/search")
	@Operation(summary = "Search a game by name endpoint")
	public ResponseEntity<Response<String, List<GameDto>>> getGames(@RequestParam("query") String query) {
		List<GameDto> gameList = gameServices.searchGames(query);

		if (gameList.size() < 1) {
			Response<String, List<GameDto>> response = new Response<>("No game found with given information's!", gameList);

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		Response<String, List<GameDto>> response = new Response<>("Games found with given information's!", gameList);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping
	@Operation(summary = "Get all games endpoint")
	public ResponseEntity<Response<String, List<GameDto>>> getAllGames() {
		List<GameDto> gameList = gameServices.getAllGames();

		Response<String, List<GameDto>> response = new Response<>("All games found!", gameList);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get a game by id endpoint")
	public ResponseEntity<Response<String, GameDto>> getGameById(@PathVariable("id") UUID id) {
		GameDto gameDto = gameServices.getGameById(id);

		Response<String, GameDto> response = new Response<>("Game found with given id!", gameDto);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Update a game by id endpoint")
	public ResponseEntity<Response<String, GameDto>> updateGame(@PathVariable("id") UUID id, @RequestBody OptionalGameDto request) {
		GameDto gameDto = gameServices.updateGame(id, request);

		Response<String, GameDto> response = new Response<>("Game updated successfully!", gameDto);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a game by id endpoint")
	public ResponseEntity<HashMap<String, String>> deleteGame(@PathVariable("id") UUID id) {
		gameServices.deleteGameById(id);

		HashMap<String, String> response = new HashMap<>();

		response.put("message", "Game deleted successfully!");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
