package com.api.nextspring.dto.optionals;

import com.api.nextspring.enums.GameRatingOptions;

import lombok.Data;

@Data
public class OptionalGameDto {
	private String name;
	private String description;
	private GameRatingOptions grade;
	private String genreId;
	private String developerId;
}
