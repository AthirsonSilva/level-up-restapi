package com.api.levelup.dto.export;

import com.api.levelup.enums.GameRatingOptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameExportDto {
	private String id;;

	private String name;

	private String description;

	private String photoPath;

	private int year;

	private GameRatingOptions grade;

	private String genreId;

	private String developerId;

	private String createdAt;

	private String updatedAt;
}
