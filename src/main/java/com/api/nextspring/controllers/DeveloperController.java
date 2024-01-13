package com.api.nextspring.controllers;

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

import com.api.nextspring.dto.DeveloperDto;
import com.api.nextspring.dto.Response;
import com.api.nextspring.dto.optionals.OptionalDeveloperDto;
import com.api.nextspring.exceptions.RestApiException;
import com.api.nextspring.services.DeveloperService;
import com.api.nextspring.services.LinkingService;

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
@RequestMapping("/api/v1/developers")
@RequiredArgsConstructor
@Tag(name = "Developer", description = "Developer endpoints for creating, getting, updating and deleting developers")
@SecurityRequirement(name = "JWT Authentication")
@ApiResponses({
		@ApiResponse(responseCode = "400", description = "Bad Request, the user did not send all required data", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "401", description = "Unauthorized, the user is not logged in or does not have access permition", content = @Content(mediaType = "application/json"))
})
public class DeveloperController {
	private final DeveloperService developerService;
	private final LinkingService linkingService;

	@PostMapping
	@Operation(summary = "Create a new developer endpoint")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Response<String, DeveloperDto>> create(@Validated @RequestBody DeveloperDto request,
			HttpServletRequest servletRequest) {
		DeveloperDto creator = developerService.create(request);

		creator = linkingService.addHateoasLinksToClass(servletRequest, "developers", creator);

		Response<String, DeveloperDto> response = new Response<>("Developer created successfully", creator);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping
	@Operation(summary = "Get all developers endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Response<String, List<DeveloperDto>>> getAll(
			@ParameterObject Pageable pageable,
			HttpServletRequest servletRequest) {
		List<DeveloperDto> creator = developerService.findAll(pageable);

		for (DeveloperDto developerDto : creator) {
			developerDto = linkingService.addHateoasLinksToClass(servletRequest, "developers", developerDto);
		}

		Response<String, List<DeveloperDto>> response = new Response<>("All developers fetched successfully", creator);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get a developer by id endpoint")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<Response<String, DeveloperDto>> get(@PathVariable String id,
			HttpServletRequest servletRequest) {
		DeveloperDto creator = developerService.findByID(id);

		creator = linkingService.addHateoasLinksToClass(servletRequest, "developers", creator);

		Response<String, DeveloperDto> response = new Response<>("Developer fetched successfully", creator);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Update a developer by id endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Response<String, Object>> update(@PathVariable String id,
			@Validated @RequestBody OptionalDeveloperDto request) {
		DeveloperDto creator = developerService.updateByID(id, request);

		Response<String, Object> response = new Response<>("Developer updated successfully", creator);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a developer by id endpoint")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<HashMap<String, Object>> delete(@PathVariable String id) {
		developerService.deleteByID(id);

		HashMap<String, Object> response = new HashMap<>();

		response.put("message", "Developer deleted successfully");

		return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
	}

	@GetMapping("/search")
	@Operation(summary = "Search a developer by name or description endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Response<String, List<DeveloperDto>>> getDevelopers(
			@RequestParam(value = "query", defaultValue = "") String query,
			@ParameterObject Pageable pageable,
			HttpServletRequest servletRequest) {
		if (query.isEmpty() || query.isBlank()) {
			throw new RestApiException(HttpStatus.BAD_REQUEST,
					"Query parameter with the developer information is required!");
		}

		List<DeveloperDto> developerList = developerService.search(query, pageable);

		if (developerList.size() == 0) {
			Response<String, List<DeveloperDto>> response = new Response<>(
					"No developer found with given information's!",
					developerList);

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		for (DeveloperDto developerDto : developerList) {
			developerDto = linkingService.addHateoasLinksToClass(servletRequest, "developers", developerDto);
		}

		Response<String, List<DeveloperDto>> response = new Response<>("Developers found with given information's!",
				developerList);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/export/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	@Operation(summary = "Export all developers in the database to excel endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> exportToExcel(HttpServletResponse response) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=developers_" + currentDateTime + ".xlsx";

		response.setContentType("application/octet-stream");
		response.setHeader(headerKey, headerValue);

		developerService.exportToExcel(response);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping(value = "/export/csv", produces = "application/csv")
	@ResponseBody
	@Operation(summary = "Export all developers in the database to csv endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> exportToCSV(HttpServletResponse response) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=developers_" + currentDateTime + ".csv";

		response.setContentType("application/csv");
		response.setHeader(headerKey, headerValue);

		developerService.exportToCSV(response);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping(value = "/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	@ResponseBody
	@Operation(summary = "Export all developers in the database to pdf endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> exportToPDF(HttpServletResponse response) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=developers_" + currentDateTime + ".pdf";

		response.setContentType("application/pdf");
		response.setHeader(headerKey, headerValue);

		developerService.exportToPDF(response);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
