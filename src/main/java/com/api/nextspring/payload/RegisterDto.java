package com.api.nextspring.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
public class RegisterDto {
	@NotEmpty(message = "Name is required")
	@Length(min = 3, max = 60, message = "Name must be between 3 and 60 characters")
	private String name;
	@NotEmpty(message = "Email is required")
	@Email(message = "Email must be valid")
	@Length(min = 3, max = 60, message = "Email must be between 3 and 60 characters")
	private String email;
	@NotEmpty(message = "CPF is required")
	@Length(min = 11, max = 14, message = "CPF must be between 11 and 14 characters")
	private String cpf;
	@NotEmpty(message = "Password is required")
	@Length(min = 6, max = 60, message = "Password must be between 6 and 60 characters")
	private String password;
	private boolean isAdmin;
}
