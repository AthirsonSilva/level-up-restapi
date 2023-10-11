package com.api.nextspring.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.nextspring.services.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin endpoint for getting the current logged in admin")
@SecurityRequirement(name = "JWT Authentication")
@ApiResponses({
		@ApiResponse(responseCode = "400", description = "Bad Request, the user did not send all required data", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "401", description = "Unauthorized, the user is not logged in", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "403", description = "Forbidden, the user does not have access permition", content = @Content(mediaType = "application/json"))
})
public class AdminController {

	private final AdminService adminService;

	@GetMapping("/check")
	@Operation(summary = "Checks if the current logged in user is an admin")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> checkAdmin() {
		return new ResponseEntity<>("You are an admin.", HttpStatus.OK);
	}

	@GetMapping(value = "/export/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	@Operation(summary = "Export all users in the database to excel endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> exportToExcel(HttpServletResponse response) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";

		response.setContentType("application/octet-stream");
		response.setHeader(headerKey, headerValue);

		adminService.exportToExcel(response);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("/export/csv")
	@Operation(summary = "Exports the users database's data to a CSV file")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> exportToCSV(HttpServletResponse response) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_" + currentDateTime + ".csv";

		response.setContentType("application/csv");
		response.setHeader(headerKey, headerValue);

		adminService.exportToCSV(response);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping(value = "/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	@ResponseBody
	@Operation(summary = "Export all users in the database to pdf endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> exportToPDF(HttpServletResponse response) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";

		response.setContentType("application/pdf");
		response.setHeader(headerKey, headerValue);

		adminService.exportToPDF(response);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
