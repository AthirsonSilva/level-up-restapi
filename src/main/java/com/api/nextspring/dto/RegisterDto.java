package com.api.nextspring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
	@NotBlank(message = "Name is required")
	@Size(min = 3, max = 60, message = "Name must be between 3 and 60 characters")
	private String name;

	@NotBlank(message = "Email is required")
	@Email(message = "Email must be valid")
	@Size(min = 3, max = 60, message = "Email must be between 3 and 60 characters")
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 6, max = 60, message = "Password must be between 6 and 60 characters")
	private String password;

	@NotBlank(message = "Password confirmation is required")
	@Size(min = 6, max = 60, message = "Password confirmation must be between 6 and 60 characters")
	private String passwordConfirmation;

	@NotBlank(message = "ZipCode is required")
	@Size(min = 8, max = 8, message = "ZipCode must be 8 characters")
	private String zipCode;
}
