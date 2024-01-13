package com.api.nextspring.dto;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserDto extends RepresentationModel<UserDto> {
	private String id;;

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

	private AddressDto address;

	private String photoPath;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
