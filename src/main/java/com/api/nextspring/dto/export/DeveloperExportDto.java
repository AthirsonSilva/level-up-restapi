package com.api.nextspring.dto.export;

import java.util.UUID;

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
public class DeveloperExportDto {
	private UUID id;

	private String name;

	private String description;

	private String createdAt;

	private String updatedAt;
}
