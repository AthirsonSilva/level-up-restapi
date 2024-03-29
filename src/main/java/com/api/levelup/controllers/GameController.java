package com.api.levelup.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.levelup.dto.GameBuyingResponse;
import com.api.levelup.dto.GameDto;
import com.api.levelup.dto.Response;
import com.api.levelup.dto.StripeTokenResponse;
import com.api.levelup.dto.optionals.OptionalGameDto;
import com.api.levelup.dto.request.GameBuyingRequest;
import com.api.levelup.dto.request.GameRequestDto;
import com.api.levelup.dto.request.StripeChargeRequest;
import com.api.levelup.exceptions.RestApiException;
import com.api.levelup.services.GameService;
import com.api.levelup.services.LinkingService;
import com.api.levelup.services.StripeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
@Tag(name = "Game", description = "Game endpoint for creating, getting, updating and deleting games")
@SecurityRequirement(name = "JWT Authentication")
@ApiResponses({
		@ApiResponse(responseCode = "400", description = "Bad Request, the user did not send all required data", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "401", description = "Unauthorized, the user is not logged in or does not have access permition", content = @Content(mediaType = "application/json"))
})
public class GameController {

	private final GameService gameServices;
	private final LinkingService linkingService;
	private final StripeService stripeService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a new game endpoint")
	public ResponseEntity<Response<String, GameDto>> createGame(@Valid @RequestBody GameRequestDto request,
			HttpServletRequest servletRequest) {
		GameDto gameDto = gameServices.create(request);

		gameDto = linkingService.addHateoasLinksToClass(servletRequest, "games", gameDto);

		Response<String, GameDto> response = new Response<>("Game created successfully!", gameDto);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/search")
	@Operation(summary = "Search a game by name endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Response<String, List<GameDto>>> searchGameByQuery(
			@RequestParam(value = "query", defaultValue = "") String query,
			@ParameterObject Pageable pageable,
			HttpServletRequest servletRequest) {
		if (query.isEmpty() || query.isBlank()) {
			throw new RestApiException(HttpStatus.BAD_REQUEST,
					"Query parameter with the game information is required!");
		}

		List<GameDto> gameList = gameServices.searchByKeyword(query, pageable);

		if (gameList.size() == 0) {
			Response<String, List<GameDto>> response = new Response<>("No game found with given information's!",
					gameList);

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		for (GameDto gameDto : gameList) {
			gameDto = linkingService.addHateoasLinksToClass(servletRequest, "games", gameDto);
		}

		Response<String, List<GameDto>> response = new Response<>("Games found with given information's!",
				gameList);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping
	@Operation(summary = "Get all games endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Response<String, List<GameDto>>> getAllGames(
			@ParameterObject Pageable pageable,
			HttpServletRequest servletRequest) {
		List<GameDto> gameList = gameServices.findAll(pageable);

		for (GameDto gameDto : gameList) {
			gameDto = linkingService.addHateoasLinksToClass(servletRequest, "games", gameDto);
		}

		Response<String, List<GameDto>> response = new Response<>("All games found!", gameList);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get a game by id endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Response<String, GameDto>> getGameById(@PathVariable(value = "id") String id,
			HttpServletRequest servletRequest) {
		GameDto gameDto = gameServices.findByID(id);

		gameDto = linkingService.addHateoasLinksToClass(servletRequest, "games", gameDto);

		Response<String, GameDto> response = new Response<>("Game found with given id!", gameDto);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Update a game by id endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Response<String, GameDto>> updateGame(
			@PathVariable(value = "id", required = true) String id,
			@RequestBody OptionalGameDto request, HttpServletRequest servletRequest) {
		GameDto gameDto = gameServices.updateById(id, request);

		gameDto = linkingService.addHateoasLinksToClass(servletRequest, "games", gameDto);

		Response<String, GameDto> response = new Response<>("Game updated successfully!", gameDto);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a game by id endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<HashMap<String, String>> deleteGame(@PathVariable(value = "id", required = true) String id,
			HttpServletRequest request) {
		gameServices.deleteById(id);

		HashMap<String, String> response = new HashMap<>();

		response.put("message", "Game deleted successfully!");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/uploadPhoto/{id}")
	@Operation(summary = "Upload a game photo by id endpoint")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Response<String, GameDto>> uploadGamePhoto(
			@PathVariable(value = "id", required = true) String id,
			@RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest servletRequest) {
		GameDto gameDto = gameServices.uploadPhoto(id, file);

		gameDto = linkingService.addHateoasLinksToClass(servletRequest, "games", gameDto);

		Response<String, GameDto> response = new Response<>("Game photo uploaded successfully!", gameDto);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping(value = "/downloadPhoto/{id}", produces = MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	@Operation(summary = "Download a game photo by id endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> downloadGameImage(@PathVariable("id") String id,
			HttpServletResponse response) {
		response.setContentType(MediaType.IMAGE_PNG_VALUE);

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=game_" + id.toString() + ".png";

		response.setHeader(headerKey, headerValue);

		gameServices.downloadPhotoByGame(id, response);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(value = "/export/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	@Operation(summary = "Export all games in the database to excel endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> exportToExcel(HttpServletResponse response) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=games_" + currentDateTime + ".xlsx";

		response.setContentType("application/octet-stream");
		response.setHeader(headerKey, headerValue);

		gameServices.exportToExcel(response);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping(value = "/export/csv", produces = "application/csv")
	@ResponseBody
	@Operation(summary = "Export all games in the database to csv endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> exportToCSV(HttpServletResponse response) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=games_" + currentDateTime + ".csv";

		response.setContentType("application/csv");
		response.setHeader(headerKey, headerValue);

		gameServices.exportToCSV(response);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping(value = "/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	@ResponseBody
	@Operation(summary = "Export all games in the database to pdf endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> exportToPDF(HttpServletResponse response) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=games_" + currentDateTime + ".pdf";

		response.setContentType("application/pdf");
		response.setHeader(headerKey, headerValue);

		gameServices.exportToPDF(response);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PostMapping("/buy")
	@ResponseBody
	@Operation(summary = "Buy a game endpoint", description = "This endpoint will create a token for the credit card and then will charge the user for the game")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Response<String, GameBuyingResponse>> createCardToken(
			@Valid @RequestBody GameBuyingRequest request) {
		StripeTokenResponse tokenDto = stripeService.createCardToken(request);
		StripeChargeRequest chargeRequest = StripeChargeRequest.builder()
				.token(tokenDto.getToken())
				.username(request.getUsername())
				.amount(request.getAmount())
				.metadata(request.getMetadata())
				.build();

		GameBuyingResponse paymentResponse = stripeService.createCharge(chargeRequest);

		Response<String, GameBuyingResponse> response = new Response<>("The game was bought successfully!",
				paymentResponse);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
