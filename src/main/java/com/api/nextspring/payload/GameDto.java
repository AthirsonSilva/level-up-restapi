package com.api.nextspring.payload;

import com.api.nextspring.enums.GradesOptions;
import lombok.Data;

import java.util.UUID;

@Data
public class GameDto {
	private String name;
	private String description;
	private int year;
	private GradesOptions grade;
	private UUID genreId;
}
