package com.api.nextspring.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class LoginDto {
	@NotEmpty(message = "Email is required")
	@Email(message = "Email must be valid")
	private String email;
	@NotEmpty(message = "Password is required")
	@Length(min = 6, max = 24, message = "Password must be between 6 and 24 characters")
	private String password;
}
