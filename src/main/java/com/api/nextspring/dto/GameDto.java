package com.api.nextspring.dto;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;

import com.api.nextspring.enums.GameRatingOptions;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GameDto extends RepresentationModel<GameDto> {
	private UUID id;

	@NotEmpty(message = "Name is required")
	@Length(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
	@Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
	private String name;

	@NotEmpty(message = "Description is required")
	@Length(min = 3, max = 50, message = "Description must be between 3 and 50 characters")
	@Size(min = 3, max = 50, message = "Description must be between 3 and 50 characters")
	private String description;

	@NotNull(message = "Year is required")
	@Min(value = 1900, message = "Year must be greater than 1900")
	private int year;

	@NotNull(message = "Grade is required")
	private GameRatingOptions grade;

	@NotNull(message = "Genre is required")
	private UUID genreId;

	@NotNull(message = "Developer is required")
	private UUID developerId;
}
