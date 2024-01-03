package com.api.nextspring.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.api.nextspring.enums.GameRatingOptions;

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
public class GameDto extends RepresentationModel<GameDto> {
	private UUID id;

	@NotBlank(message = "Name is required")
	@Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
	private String name;

	@NotBlank(message = "Description is required")
	@Size(min = 3, max = 50, message = "Description must be between 3 and 50 characters")
	private String description;

	private String photoPath;

	@NotNull(message = "Year is required")
	@Min(value = 1900, message = "Year must be greater than 1900")
	private int year;

	@NotNull(message = "Grade is required")
	private GameRatingOptions grade;

	@NotNull(message = "Genre is required")
	private UUID genreId;

	@NotNull(message = "Developer is required")
	private UUID developerId;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
