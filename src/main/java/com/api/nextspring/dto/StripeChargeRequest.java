package com.api.nextspring.dto;

import java.util.HashMap;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StripeChargeRequest {

	@NotBlank(message = "Username is required")
	private String token;

	@NotBlank(message = "Username is required")
	@Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
	private String username;

	@NotNull(message = "Amount is required")
	@Min(value = 1, message = "Amount must be greater than 0")
	private Double amount;

	private HashMap<String, String> metadata;
}
