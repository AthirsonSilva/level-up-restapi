package com.api.nextspring.dto.export;

import java.util.UUID;

import com.api.nextspring.enums.GameRatingOptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameExportDto {
	private UUID id;

	private String name;

	private String description;

	private String photoPath;

	private int year;

	private GameRatingOptions grade;

	private UUID genreId;

	private UUID developerId;

	private String createdAt;

	private String updatedAt;
}
