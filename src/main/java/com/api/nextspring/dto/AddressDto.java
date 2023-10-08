package com.api.nextspring.dto;

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
public class AddressDto {
	private UUID id;

	private String street;

	private String complement;

	private String neighborhood;

	private String city;

	private String state;

	private String zipCode;
}
