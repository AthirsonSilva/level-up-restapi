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
public class UserExportDto {
	private UUID id;

	private String name;

	private String email;

	private String zipCode;

	private String street;

	private String complement;

	private String neighborhood;

	private String city;

	private String state;

	private String photoPath;

	private String createdAt;

	private String updatedAt;
}
