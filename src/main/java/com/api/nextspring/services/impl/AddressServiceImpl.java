package com.api.nextspring.services.impl;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.api.nextspring.dto.AddressDto;
import com.api.nextspring.dto.ViaCepResponseDto;
import com.api.nextspring.entity.AddressEntity;
import com.api.nextspring.exceptions.RestApiException;
import com.api.nextspring.repositories.AddressRepository;
import com.api.nextspring.services.AddressService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * This class implements the AddressService interface and provides methods to
 * create, retrieve and update addresses.
 * It uses the AddressRepository to interact with the database and the Viacep
 * API to retrieve address information by zip code.
 * 
 * @see AddressService
 * @param addressRepository the repository used to interact with the database
 * @implNote This class implements the AddressService interface and provides
 * 
 * @author Athirson Silva
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class AddressServiceImpl implements AddressService {

	private final AddressRepository addressRepository;
	private final ObjectMapper objectMapper;

	/**
	 * Creates a new address.
	 *
	 * @param addressDto the DTO containing the address information
	 * @return the DTO of the created address
	 */
	@Override
	public AddressDto create(AddressDto addressDto) {
		log.info("Creating address: {}", addressDto);

		return null;
	}

	/**
	 * Retrieves an address by its zip code.
	 *
	 * @param zipCode the zip code of the address to retrieve
	 * @return the DTO of the retrieved address
	 */
	@Override
	public AddressEntity getByZipCode(String zipCode) {
		log.info("Retrieving address by zip code: {}", zipCode);

		// Regex for zip code validation
		String zipCodeRegex = "\\d{8}";

		// Check if the zip code is valid
		if (!zipCode.matches(zipCodeRegex)) {
			log.error("Invalid zip code: {}", zipCode);
			return null;
		}

		try {
			// ViaCep API base URL
			String url = "https://viacep.com.br/ws/" + zipCode + "/json/";

			// Create a new HTTP client and request
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest
					.newBuilder()
					.uri(URI.create(url))
					.build();

			// Send the request and get the response body as a string
			HttpResponse<String> response = client.send(
					request,
					HttpResponse.BodyHandlers.ofString());

			log.info("Response status code: {}", response.statusCode());
			log.info("Response body: {}", response.body());

			// Check if the response status code is 200 (OK)
			if (response.statusCode() != 200) {
				throw new RestApiException(HttpStatus.BAD_REQUEST,
						"Given zip code does not exist. It was not possible to retrieve address information.");
			}

			// Map the response body to a ViaCepResponseDto object
			ViaCepResponseDto viaCepResponseDto = objectMapper
					.readValue(response.body(), ViaCepResponseDto.class);

			log.info("ViaCepResponseDto: {}", viaCepResponseDto);

			// Map the ViaCepResponseDto object to an AddressEntity object
			AddressEntity addressEntity = AddressEntity
					.builder()
					.zipCode(viaCepResponseDto.getCep())
					.street(viaCepResponseDto.getLogradouro())
					.complement(viaCepResponseDto.getComplemento())
					.neighborhood(viaCepResponseDto.getBairro())
					.city(viaCepResponseDto.getLocalidade())
					.state(viaCepResponseDto.getUf())
					.build();

			log.info("AddressEntity: {}", addressEntity);

			// Save the address to the database and return it
			AddressEntity savedAddress = addressRepository.save(addressEntity);

			log.info("Saved address: {}", savedAddress);

			return savedAddress;
		} catch (IOException | InterruptedException e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error while retrieving address by zip code");
		}
	}

	/**
	 * Updates an existing address.
	 *
	 * @param addressDto the DTO containing the updated address information
	 * @return the DTO of the updated address
	 */
	@Override
	public AddressDto update(AddressDto addressDto) {
		log.info("Updating address: {}", addressDto);

		return null;
	}
}
