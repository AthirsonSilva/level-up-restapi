package com.api.nextspring.services.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.api.nextspring.dto.DeveloperDto;
import com.api.nextspring.dto.export.DeveloperExportDto;
import com.api.nextspring.dto.optionals.OptionalDeveloperDto;
import com.api.nextspring.entity.DeveloperEntity;
import com.api.nextspring.enums.EntityOptions;
import com.api.nextspring.exceptions.RestApiException;
import com.api.nextspring.repositories.DeveloperRepository;
import com.api.nextspring.services.DeveloperService;
import com.api.nextspring.utils.CsvUtils;
import com.api.nextspring.utils.ExcelUtils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * This class implements the DeveloperService interface and provides the
 * implementation for the CRUD operations
 * related to developers. It uses DeveloperRepository to interact with the
 * database and ModelMapper to map between
 * DTOs and entities. It also provides an implementation for exporting developer
 * data to an Excel file using ExcelUtils.
 * 
 * @author Athirson Silva
 * @implNote This class implements the UserService interface and provides the
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class DeveloperServiceImpl implements DeveloperService {
	private final DeveloperRepository developerRepository;
	private final ModelMapper modelMapper;
	private final ExcelUtils excelUtils;
	private final CsvUtils csvUtils;

	/**
	 * Creates a new developer with the given name and description.
	 *
	 * @param request the DeveloperDto object containing the name and description of
	 *                the developer to be created.
	 * @return the DeveloperDto object representing the newly created developer.
	 * @throws RestApiException if a developer with the given name already exists.
	 */
	public DeveloperDto create(DeveloperDto request) {
		if (developerRepository.existsByName(request.getName())) {
			throw new RestApiException(HttpStatus.NOT_FOUND, "Developer with given name already exists");
		}

		DeveloperEntity developerEntity = DeveloperEntity
				.builder()
				.name(request.getName())
				.description(request.getDescription())
				.build();

		return modelMapper.map(developerRepository.save(developerEntity), DeveloperDto.class);
	}

	/**
	 * Finds all developers in the database and returns them as a list of
	 * DeveloperDto objects.
	 *
	 * @param page      the page number of the results to return.
	 * @param size      the number of results to return per page.
	 * @param sort      the field to sort the results by.
	 * @param direction the direction to sort the results in (ASC or DESC).
	 * @return a list of DeveloperDto objects representing the developers found in
	 *         the database.
	 * @throws RestApiException if no developers are found in the database.
	 */
	public List<DeveloperDto> findAll(Integer page, Integer size, String sort, String direction) {
		Pageable pageable = PageRequest
				.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));

		List<DeveloperEntity> developerEntities = developerRepository.findAll(pageable).toList();

		if (developerEntities.isEmpty())
			throw new RestApiException(HttpStatus.NOT_FOUND, "No developers found!");

		return developerEntities.stream().map(
				developerEntity -> modelMapper.map(developerEntity, DeveloperDto.class)).collect(Collectors.toList());
	}

	/**
	 * Finds the developer with the given ID in the database and returns it as a
	 * DeveloperDto object.
	 *
	 * @param id the ID of the developer to find.
	 * @return a DeveloperDto object representing the developer found in the
	 *         database.
	 * @throws RestApiException if no developer with the given ID is found in the
	 *                          database.
	 */
	public DeveloperDto findByID(UUID id) {
		DeveloperEntity developerEntity = developerRepository.findById(id)
				.orElseThrow(() -> new RestApiException(HttpStatus.NOT_FOUND, "Developer with given name not found"));

		return modelMapper.map(developerEntity, DeveloperDto.class);
	}

	/**
	 * Updates the developer with the given ID in the database with the new name
	 * and/or description.
	 *
	 * @param id      the ID of the developer to update.
	 * @param request the OptionalDeveloperDto object containing the new name and/or
	 *                description of the developer.
	 * @return a DeveloperDto object representing the updated developer.
	 * @throws RestApiException if no developer with the given ID is found in the
	 *                          database.
	 */
	public DeveloperDto updateByID(UUID id, OptionalDeveloperDto request) {
		DeveloperEntity developerEntity = developerRepository.findById(id)
				.orElseThrow(() -> new RestApiException(HttpStatus.NOT_FOUND, "Developer with given id not found"));

		if (request.getName() != null)
			developerEntity.setName(request.getName());

		if (request.getDescription() != null)
			developerEntity.setDescription(request.getDescription());

		return modelMapper.map(developerRepository.save(developerEntity), DeveloperDto.class);
	}

	/**
	 * Deletes the developer with the given ID from the database.
	 *
	 * @param id the ID of the developer to delete.
	 * @throws RestApiException if no developer with the given ID is found in the
	 *                          database.
	 */
	public void deleteByID(UUID id) {
		DeveloperEntity developerEntity = developerRepository.findById(id)
				.orElseThrow(() -> new RestApiException(HttpStatus.NOT_FOUND, "Developer with given name not found"));

		developerRepository.delete(developerEntity);
	}

	/**
	 * Searches for developers in the database with the given keyword and returns
	 * them as a list of DeveloperDto objects.
	 *
	 * @param query     the keyword to search for.
	 * @param page      the page number of the results to return.
	 * @param size      the number of results to return per page.
	 * @param sort      the field to sort the results by.
	 * @param direction the direction to sort the results in (ASC or DESC).
	 * @return a list of DeveloperDto objects representing the developers found in
	 *         the database.
	 * @throws RestApiException if no developers are found in the database with the
	 *                          given keyword.
	 */
	@Override
	public List<DeveloperDto> search(String query, Integer page, Integer size, String sort, String direction) {
		Pageable pageable = PageRequest
				.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));

		List<DeveloperEntity> developerEntities = developerRepository
				.searchDeveloperEntities(query, pageable)
				.toList();

		if (developerEntities.isEmpty())
			throw new RestApiException(HttpStatus.NOT_FOUND, "No developers found with given keyword!");

		return developerEntities.stream()
				.map(developerEntity -> modelMapper.map(developerEntity, DeveloperDto.class))
				.toList();
	}

	/**
	 * Exports all developers in the database to an Excel file and sends it as a
	 * response to the client.
	 *
	 * @param response the HttpServletResponse object representing the response to
	 *                 send to the client.
	 * @throws RestApiException if an error occurs while exporting the data to the
	 *                          Excel file.
	 */
	@Override
	public void exportToExcel(HttpServletResponse response) {
		List<DeveloperEntity> entityList = developerRepository.findAll();

		try {
			excelUtils.export(response, entityList, EntityOptions.DEVELOPER);
		} catch (IllegalArgumentException e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occurred while exporting data to Excel file: " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occurred while exporting data to Excel file: " + e.getMessage());
		}
	}

	/**
	 * Exports the data to a CSV file and sends it as a response to the client.
	 *
	 * @param response the HttpServletResponse object used to send the response to
	 *                 the client
	 */
	@Override
	public void exportToCSV(HttpServletResponse response) {
		// Gets all the developers from the database
		List<DeveloperEntity> entityList = developerRepository.findAll();

		// Converts the entity list to a DTO list
		Iterable<DeveloperExportDto> developerExportDtoList = entityList.stream()
				.map(developerEntity -> modelMapper.map(developerEntity, DeveloperExportDto.class))
				.collect(Collectors.toList());

		// Gets the class of the DTO
		Class<?> clazz = DeveloperExportDto.class;

		// Gets the fields of the DTO
		Field[] fieldsHeaders = clazz.getDeclaredFields();

		// Creates an array of strings with the headers of the fields
		String[] headers = new String[fieldsHeaders.length];

		// Gets the name of the fields and adds them to the headers array
		for (int i = 0; i < fieldsHeaders.length; i++) {
			headers[i] = fieldsHeaders[i].getName();
		}

		// Copies the headers to the fields array
		String[] fields = headers.clone();

		csvUtils.export(response, developerExportDtoList, headers, fields);
	}
}
