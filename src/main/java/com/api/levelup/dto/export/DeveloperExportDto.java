package com.api.levelup.dto.export;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperExportDto {
	private String id;;

	private String name;

	private String description;

	private String createdAt;

	private String updatedAt;
}
