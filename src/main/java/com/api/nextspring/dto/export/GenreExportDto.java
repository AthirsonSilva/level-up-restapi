package com.api.nextspring.dto.export;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenreExportDto {
	private UUID id;

	private String name;

	private String description;

	private String createdAt;

	private String updatedAt;
}
