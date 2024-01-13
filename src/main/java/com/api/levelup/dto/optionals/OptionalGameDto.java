package com.api.levelup.dto.optionals;

import com.api.levelup.enums.GameRatingOptions;

import lombok.Data;

@Data
public class OptionalGameDto {
	private String name;
	private String description;
	private GameRatingOptions grade;
	private String genreId;
	private String developerId;
}
