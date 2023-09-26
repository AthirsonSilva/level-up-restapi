package com.api.nextspring.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.nextspring.dto.Response;
import com.api.nextspring.dto.UserDto;
import com.api.nextspring.dto.optionals.OptionalUserDto;
import com.api.nextspring.services.LinkingService;
import com.api.nextspring.services.UserService;
import com.api.nextspring.utils.JwtTokenUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User endpoint for getting and updating the current logged in user")
public class UserController {
	private final UserService userServices;
	private final JwtTokenUtils jwtTokenUtils;
	private final LinkingService linkingService;

	@GetMapping
	@Operation(summary = "Get the current logged in user")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	@ApiResponses({
			@ApiResponse(responseCode = "400", description = "Bad Request, the user did not send all required data", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "401", description = "Unauthorized, the user is not logged in or does not have access permition", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Response<String, UserDto>> getCurrentUser(@RequestHeader Map<String, String> headers,
			HttpServletRequest servletRequest) {
		String token = jwtTokenUtils.execute(headers);

		UserDto currentUser = userServices.getCurrentUser(token);

		currentUser = linkingService.addHateoasLinksToClass(servletRequest, "users", currentUser);

		Response<String, UserDto> response = new Response<>("Current user found successfully!", currentUser);

		return ResponseEntity.ok(response);
	}

	@PatchMapping
	@Operation(summary = "Update the current logged in user")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	@ApiResponses({
			@ApiResponse(responseCode = "400", description = "Bad Request, the user did not send all required data", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "401", description = "Unauthorized, the user is not logged in or does not have access permition", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Response<String, UserDto>> updateCurrentUser(@RequestHeader Map<String, String> headers,
			@RequestBody OptionalUserDto request) {
		String token = jwtTokenUtils.execute(headers);

		UserDto updatedCurrentUser = userServices.updateCurrentUser(token, request);

		updatedCurrentUser = linkingService.addHateoasLinksToClass(null, "users", updatedCurrentUser);

		Response<String, UserDto> response = new Response<>("Current user updated successfully!", updatedCurrentUser);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping
	@Operation(summary = "Delete the current logged in user")
	@ResponseStatus(HttpStatus.OK)
	@SecurityRequirement(name = "JWT Authentication")
	@ApiResponses({
			@ApiResponse(responseCode = "400", description = "Bad Request, the user did not send all required data", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "401", description = "Unauthorized, the user is not logged in or does not have access permition", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<HashMap<String, String>> deleteUser(@RequestHeader Map<String, String> headers) {
		String token = jwtTokenUtils.execute(headers);

		userServices.deleteCurrentUser(token);

		HashMap<String, String> response = new HashMap<>();

		response.put("message", "Current user deleted successfully!");

		return ResponseEntity.ok(response);
	}
}
