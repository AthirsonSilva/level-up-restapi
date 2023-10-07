package com.api.nextspring.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.nextspring.dto.GenreDto;
import com.api.nextspring.dto.Response;
import com.api.nextspring.dto.optionals.OptionalGenreDto;
import com.api.nextspring.exceptions.RestApiException;
import com.api.nextspring.services.LinkingService;
import com.api.nextspring.services.impl.GenreServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
@Tag(name = "Genre", description = "Genre endpoint for getting, creating, updating and deleting genres")
public class GenreController {
	private final GenreServiceImpl genreServices;
	private final LinkingService linkingService;

	@GetMapping
	@Operation(summary = "Get all genres endpoint")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	@ApiResponses({
			@ApiResponse(responseCode = "400", description = "Bad Request, the user did not send all required data", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "401", description = "Unauthorized, the user is not logged in or does not have access permition", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Response<String, List<GenreDto>>> getAllGenres(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "sort", defaultValue = "name") String sort,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			HttpServletRequest servletRequest) {
		List<GenreDto> genreList = genreServices.findAll(page, size, sort, direction);

		for (GenreDto genreDto : genreList) {
			genreDto = linkingService.addHateoasLinksToClass(servletRequest, "genres", genreDto);
		}

		Response<String, List<GenreDto>> response = new Response<>("Genres fetched successfully!", genreList);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get a genre by id endpoint")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	@ApiResponses({
			@ApiResponse(responseCode = "400", description = "Bad Request, the user did not send all required data", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "401", description = "Unauthorized, the user is not logged in or does not have access permition", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Response<String, GenreDto>> getGenreById(@PathVariable UUID id,
			HttpServletRequest servletRequest) {
		GenreDto genreDto = genreServices.findByID(id);

		genreDto = linkingService.addHateoasLinksToClass(servletRequest, "genres", genreDto);

		Response<String, GenreDto> response = new Response<>("Genre fetched successfully!", genreDto);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/search")
	@Operation(summary = "Search a genre by name endpoint")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	@ApiResponses({
			@ApiResponse(responseCode = "400", description = "Bad Request, the user did not send all required data", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "401", description = "Unauthorized, the user is not logged in or does not have access permition", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Response<String, List<GenreDto>>> searchGenresByQuery(
			@RequestParam(value = "query", defaultValue = "") String query,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "sort", defaultValue = "name") String sort,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			HttpServletRequest servletRequest) {
		if (query.isEmpty() || query.isBlank()) {
			throw new RestApiException(HttpStatus.BAD_REQUEST,
					"Query parameter with the genre information is required!");
		}

		List<GenreDto> genreDtoList = genreServices.searchByKeyword(query, page, size, sort, direction);

		if (genreDtoList.size() == 0) {
			Response<String, List<GenreDto>> response = new Response<>("No genre found with given information's!",
					genreDtoList);

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		for (GenreDto genre : genreDtoList) {
			genre = linkingService.addHateoasLinksToClass(servletRequest, "genres", genre);
		}

		Response<String, List<GenreDto>> response = new Response<>("Genres with given information fetched successfully!",
				genreDtoList);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping
	@Operation(summary = "Create a genre endpoint")
	@ResponseStatus(HttpStatus.CREATED)
	@SecurityRequirement(name = "JWT Authentication")
	@ApiResponses({
			@ApiResponse(responseCode = "400", description = "Bad Request, the user did not send all required data", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "401", description = "Unauthorized, the user is not logged in or does not have access permition", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Response<String, GenreDto>> createGenre(@Validated @RequestBody GenreDto request,
			HttpServletRequest servletRequest) {
		GenreDto genreDto = genreServices.create(request);

		genreDto = linkingService.addHateoasLinksToClass(servletRequest, "genres", genreDto);

		Response<String, GenreDto> response = new Response<>("Genre created successfully!", genreDto);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a genre by id endpoint")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	@ApiResponses({
			@ApiResponse(responseCode = "400", description = "Bad Request, the user did not send all required data", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "401", description = "Unauthorized, the user is not logged in or does not have access permition", content = @Content(mediaType = "application/json"))
	})
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
	@ApiResponses({
			@ApiResponse(responseCode = "400", description = "Bad Request, the user did not send all required data", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "401", description = "Unauthorized, the user is not logged in or does not have access permition", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Response<String, GenreDto>> updateGenre(@Validated @RequestBody OptionalGenreDto request,
			@PathVariable UUID id, HttpServletRequest servletRequest) {
		GenreDto genreDto = genreServices.updateByID(id, request);

		genreDto = linkingService.addHateoasLinksToClass(servletRequest, "genres", genreDto);

		Response<String, GenreDto> response = new Response<>("Genre updated successfully!", genreDto);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/export/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	@Operation(summary = "Export all genres in the database to excel endpoint")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	@ApiResponses({
			@ApiResponse(responseCode = "400", description = "Bad Request, the user did not send all required data", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "401", description = "Unauthorized, the user is not logged in or does not have access permition", content = @Content(mediaType = "application/json"))
	})
	public void exportToExcel(HttpServletResponse response) {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=genres_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		genreServices.exportToExcel(response);
	}
}
