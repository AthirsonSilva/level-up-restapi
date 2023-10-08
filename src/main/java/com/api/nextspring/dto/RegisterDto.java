package com.api.nextspring.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
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
	@NotEmpty(message = "Name is required")
	@Length(min = 3, max = 60, message = "Name must be between 3 and 60 characters")
	@Size(min = 3, max = 60, message = "Name must be between 3 and 60 characters")
	private String name;

	@NotEmpty(message = "Email is required")
	@Email(message = "Email must be valid")
	@Length(min = 3, max = 60, message = "Email must be between 3 and 60 characters")
	@Size(min = 3, max = 60, message = "Email must be between 3 and 60 characters")
	private String email;

	@NotEmpty(message = "Password is required")
	@Length(min = 6, max = 60, message = "Password must be between 6 and 60 characters")
	@Size(min = 6, max = 60, message = "Password must be between 6 and 60 characters")
	private String password;

	@NotEmpty(message = "Password confirmation is required")
	@Length(min = 6, max = 60, message = "Password confirmation must be between 6 and 60 characters")
	@Size(min = 6, max = 60, message = "Password confirmation must be between 6 and 60 characters")
	private String passwordConfirmation;

	@NotEmpty(message = "ZipCode is required")
	@Length(min = 8, max = 8, message = "ZipCode must be 8 characters")
	@Size(min = 8, max = 8, message = "ZipCode must be 8 characters")
	private String zipCode;
}
