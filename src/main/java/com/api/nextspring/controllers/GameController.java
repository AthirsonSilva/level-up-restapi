package com.api.nextspring.controllers;

import com.api.nextspring.payload.GameDto;
import com.api.nextspring.payload.Response;
import com.api.nextspring.services.GameServices;
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
public class GameController {
	private final GameServices gameServices;

	@PostMapping
	public ResponseEntity<Response<String, GameDto>> createRestaurant(@RequestBody GameDto request) {
		GameDto gameDto = gameServices.createGame(request);

		Response<String, GameDto> response = new Response<>("Restaurant created successfully!", gameDto);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/search")
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
	public ResponseEntity<Response<String, List<GameDto>>> getAllGames() {
		List<GameDto> gameList = gameServices.getAllGames();

		Response<String, List<GameDto>> response = new Response<>("All games found!", gameList);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Response<String, GameDto>> getGameById(@PathVariable("id") UUID id) {
		GameDto gameDto = gameServices.getGameById(id);

		Response<String, GameDto> response = new Response<>("Game found with given id!", gameDto);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Response<String, GameDto>> updateGame(@PathVariable("id") UUID id, @RequestBody GameDto request) {
		GameDto gameDto = gameServices.updateGame(id, request);

		Response<String, GameDto> response = new Response<>("Game updated successfully!", gameDto);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<HashMap<String, String>> deleteGame(@PathVariable("id") UUID id) {
		gameServices.deleteGameById(id);

		HashMap<String, String> response = new HashMap<>();

		response.put("message", "Game deleted successfully!");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
