package com.api.nextspring.dto;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserDto extends RepresentationModel<UserDto> {
	private UUID id;

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
}
