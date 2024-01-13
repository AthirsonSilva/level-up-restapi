package com.api.levelup.dto.export;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserExportDto {
	private String id;;

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
