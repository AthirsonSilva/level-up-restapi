package com.api.nextspring.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

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
public class GenreDto extends RepresentationModel<GenreDto> {
	private String id;;

	@NotBlank(message = "Name is required")
	@Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
	private String name;

	@NotBlank(message = "Description is required")
	@Size(min = 3, message = "Description must be at least 3 characters")
	private String description;

	private List<GameDto> games;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
