package com.api.nextspring.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.nextspring.dto.ChangePasswordDto;
import com.api.nextspring.dto.Response;
import com.api.nextspring.dto.UserDto;
import com.api.nextspring.dto.optionals.OptionalUserDto;
import com.api.nextspring.services.LinkingService;
import com.api.nextspring.services.UserService;
import com.api.nextspring.utils.JwtTokenExtracter;

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
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User endpoint for getting and updating the current logged in user")
@SecurityRequirement(name = "JWT Authentication")
@ApiResponses({
		@ApiResponse(responseCode = "400", description = "Bad Request, the user did not send all required data", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "401", description = "Unauthorized, the user is not logged in or does not have access permition", content = @Content(mediaType = "application/json"))
})
public class UserController {
	private final UserService userServices;
	private final JwtTokenExtracter jwtTokenExtracter;
	private final LinkingService linkingService;

	@GetMapping
	@Operation(summary = "Get the current logged in user")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Response<String, UserDto>> getCurrentUser(@RequestHeader Map<String, String> headers,
			HttpServletRequest servletRequest) {
		String token = jwtTokenExtracter.execute(headers);

		UserDto currentUser = userServices.getCurrentUser(token);

		currentUser = linkingService.addHateoasLinksToClass(servletRequest, "users", currentUser);

		Response<String, UserDto> response = new Response<>("Current user found successfully!", currentUser);

		return ResponseEntity.ok(response);
	}

	@PatchMapping
	@Operation(summary = "Update the current logged in user")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Response<String, UserDto>> updateCurrentUser(@RequestHeader Map<String, String> headers,
			@RequestBody OptionalUserDto request) {
		String token = jwtTokenExtracter.execute(headers);

		UserDto updatedCurrentUser = userServices.updateCurrentUser(token, request);

		updatedCurrentUser = linkingService.addHateoasLinksToClass(null, "users", updatedCurrentUser);

		Response<String, UserDto> response = new Response<>("Current user updated successfully!", updatedCurrentUser);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping
	@Operation(summary = "Delete the current logged in user")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<HashMap<String, String>> deleteUser(@RequestHeader Map<String, String> headers) {
		String token = jwtTokenExtracter.execute(headers);

		userServices.deleteCurrentUser(token);

		HashMap<String, String> response = new HashMap<>();

		response.put("message", "Current user deleted successfully!");

		return ResponseEntity.ok(response);
	}

	@PostMapping("/password/change")
	@Operation(summary = "Change the current logged in user password")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Response<String, UserDto>> changeCurrentUserPassword(
			@RequestHeader Map<String, String> headers,
			@RequestBody ChangePasswordDto request) {
		String token = jwtTokenExtracter.execute(headers);

		UserDto updatedCurrentUser = userServices.changeCurrentUserPassword(token, request);

		Response<String, UserDto> response = new Response<>("Current user password changed successfully!",
				updatedCurrentUser);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/password/reset")
	@Operation(summary = "Reset the current logged in user password")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Response<String, ?>> resetCurrentUserPassword(
			@RequestHeader Map<String, String> headers) {
		String token = jwtTokenExtracter.execute(headers);

		userServices.resetCurrentUserPassword(token);

		Response<String, String> response = new Response<>("Current user password reseted successfully.",
				"The new password was sent to your email!");

		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/export/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	@Operation(summary = "Export all users in the database to excel endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> exportToExcel(HttpServletResponse response) {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		userServices.exportToExcel(response);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PostMapping("/uploadPhoto/{id}")
	@Operation(summary = "Upload a user photo by id endpoint")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Response<String, UserDto>> uploadGamePhoto(
			@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestHeader Map<String, String> headers,
			HttpServletRequest servletRequest) {
		String token = jwtTokenExtracter.execute(headers);

		UserDto userDto = userServices.uploadPhoto(token, file);

		userDto = linkingService.addHateoasLinksToClass(servletRequest, "users", userDto);

		Response<String, UserDto> response = new Response<>("User photo uploaded successfully!", userDto);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/downloadPhoto/{id}")
	@ResponseBody
	@Operation(summary = "Download a user photo by id endpoint")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<InputStreamResource> getImageDynamicType(@PathVariable("id") UUID id) {
		InputStreamResource image = userServices.downloadPhotoByUser(id);

		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
	}
}
