package com.api.nextspring.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDto {
	@Size(min = 1, max = 255, message = "The 'sender' field must be between 1 and 255 characters")
	@Length(min = 1, max = 255, message = "The 'sender' field must be between 1 and 255 characters")
	@NotEmpty(message = "The 'sender' field is required")
	private String username;

	@Size(min = 1, max = 255, message = "The 'to' field must be between 1 and 255 characters")
	@Length(min = 1, max = 255, message = "The 'to' field must be between 1 and 255 characters")
	@NotEmpty(message = "The 'to' field is required")
	private String destination;

	@Size(min = 1, max = 255, message = "The 'from' field must be between 1 and 255 characters")
	@Length(min = 1, max = 255, message = "The 'from' field must be between 1 and 255 characters")
	@NotEmpty(message = "The 'from' field is required")
	private String sender;

	@Size(min = 1, max = 255, message = "The 'content' field must be between 1 and 255 characters")
	@Length(min = 1, max = 255, message = "The 'content' field must be between 1 and 255 characters")
	@NotEmpty(message = "The 'content' field is required")
	private String content;

	@Size(min = 1, max = 255, message = "The 'subject' field must be between 1 and 255 characters")
	@Length(min = 1, max = 255, message = "The 'subject' field must be between 1 and 255 characters")
	@NotEmpty(message = "The 'subject' field is required")
	private String subject;
}