package com.api.levelup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StripeTokenResponse {

	@NotBlank(message = "Card number is required")
	@Size(min = 16, max = 16, message = "Card number must be 16 digits")
	private String cardNumber;

	@NotBlank(message = "Expiration month is required")
	@Size(min = 2, max = 2, message = "Expiration month must be 2 digits")
	private String expirationMonth;

	@NotBlank(message = "Expiration year is required")
	@Size(min = 4, max = 4, message = "Expiration year must be 4 digits")
	private String expirationYear;

	@NotBlank(message = "CVC is required")
	@Size(min = 3, max = 3, message = "CVC must be 3 digits")
	private String cvc;

	private String token;

	@NotBlank(message = "Username is required")
	@Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
	private String username;

	private Boolean success;
}
