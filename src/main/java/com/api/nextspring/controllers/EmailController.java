package com.api.nextspring.controllers;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.nextspring.dto.EmailDto;
import com.api.nextspring.services.EmailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
@Tag(name = "Email", description = "Email endpoints for sending emails")
public class EmailController {
	private final EmailService emailService;

	@PostMapping("/send")
	@Operation(summary = "Send an email to the user")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	@ApiResponses({
			@ApiResponse(responseCode = "400", description = "Bad Request, the user did not send all required data", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "401", description = "Unauthorized, the user is not logged in or does not have access permition", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<HashMap<String, String>> sendEmail(@Validated @RequestBody EmailDto request) {
		emailService.sendEmail(request);

		HashMap<String, String> response = new HashMap<>();
		response.put("message", "Email sent successfully!!");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
