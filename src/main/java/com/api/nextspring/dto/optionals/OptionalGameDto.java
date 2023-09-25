package com.api.nextspring.dto.optionals;

import com.api.nextspring.enums.GameRatingOptions;
import lombok.Data;

import java.util.UUID;

@Data
public class OptionalGameDto {
	private String name;
	private String description;
	private GameRatingOptions grade;
	private UUID genreId;
	private UUID developerId;
}
