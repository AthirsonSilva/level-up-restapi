package com.api.nextspring.payload;

import com.api.nextspring.enums.GameRatingOptions;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Data
public class GameDto {
	@NotEmpty(message = "Name is required")
	@Length(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
	private String name;
	@NotEmpty(message = "Description is required")
	@Length(min = 3, max = 50, message = "Description must be between 3 and 50 characters")
	private String description;
	@NotNull(message = "Year is required")
	private int year;
	@NotNull(message = "Grade is required")
	private GameRatingOptions grade;
	@NotNull(message = "Genre is required")
	@org.hibernate.validator.constraints.UUID(message = "Genre must be a valid UUID")
	private UUID genreId;
}
