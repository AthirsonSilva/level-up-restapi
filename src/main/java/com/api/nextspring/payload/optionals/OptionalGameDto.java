package com.api.nextspring.payload.optionals;

import com.api.nextspring.enums.GradesOptions;
import lombok.Data;

import java.util.UUID;

@Data
public class OptionalGameDto {
	private String name;
	private String description;
	private GradesOptions grade;
	private UUID genreId;
	private UUID developerId;
}
