package com.api.levelup.services;

import com.api.levelup.dto.AddressDto;
import com.api.levelup.entity.AddressEntity;

/**
 * This interface defines the methods to be implemented by a service that
 * handles address-related operations.
 */
public interface AddressService {

	/**
	 * Creates a new address based on the provided data.
	 * 
	 * @param addressDto The DTO containing the data for the new address.
	 * @return The DTO representing the newly created address.
	 */
	AddressDto create(AddressDto addressDto);

	/**
	 * Updates an existing address based on the provided data.
	 * 
	 * @param addressDto The DTO containing the updated data for the address.
	 * @return The DTO representing the updated address.
	 */
	AddressDto update(AddressDto addressDto);

	/**
	 * Retrieves an address based on its zip code.
	 * 
	 * @param zipCode The zip code of the address to retrieve.
	 * @return The DTO representing the retrieved address.
	 */
	AddressEntity getByZipCode(String zipCode);
}
