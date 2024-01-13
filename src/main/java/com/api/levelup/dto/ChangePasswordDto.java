package com.api.levelup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {

	@NotBlank(message = "Current password is required")
	@Size(min = 6, max = 20, message = "Current password must be between 8 and 20 characters")
	private String currentPassword;

	@NotBlank(message = "New password is required")
	@Size(min = 6, max = 20, message = "New password must be between 8 and 20 characters")
	private String newPassword;
}
