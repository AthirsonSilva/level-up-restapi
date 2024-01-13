package com.api.levelup.dto;

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
public class LoginDto {
	@NotBlank(message = "Email is required")
	@Email(message = "Email must be valid")
	@Size(min = 3, max = 60, message = "Email must be between 3 and 60 characters")
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 6, max = 60, message = "Password must be between 6 and 60 characters")
	private String password;
}
