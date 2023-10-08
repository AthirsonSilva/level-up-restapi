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

import com.api.nextspring.dto.GenreDto;
import com.api.nextspring.dto.export.GenreExportDto;
import com.api.nextspring.dto.optionals.OptionalGenreDto;
import com.api.nextspring.entity.GenreEntity;
import com.api.nextspring.enums.EntityOptions;
import com.api.nextspring.exceptions.RestApiException;
import com.api.nextspring.repositories.GenreRepository;
import com.api.nextspring.services.GenreService;
import com.api.nextspring.utils.CsvUtils;
import com.api.nextspring.utils.ExcelUtils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * This class implements the GenreService interface and provides the
 * implementation for all its methods.
 * It uses GenreRepository, ModelMapper, and ExcelUtils to perform CRUD
 * operations on GenreEntity objects.
 * 
 * @author Athirson Silva
 * @implNote This class implements the UserService interface and provides the
 */
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
	private final GenreRepository genreRepository;
	private final ModelMapper modelMapper;
	private final ExcelUtils excelUtils;
	private final CsvUtils csvUtils;

	/**
	 * Returns a list of all genres, sorted and paginated according to the given
	 * parameters.
	 *
	 * @param page      the page number
	 * @param size      the page size
	 * @param sort      the sort field
	 * @param direction the sort direction
	 * @return a list of GenreDto objects
	 * @throws RestApiException if no genres are found
	 */
	public List<GenreDto> findAll(Integer page, Integer size, String sort, String direction) {
		Pageable pageable = PageRequest
				.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));

		List<GenreEntity> genreEntities = genreRepository.findAll(pageable).toList();

		if (genreEntities.isEmpty())
			throw new RestApiException(HttpStatus.NOT_FOUND, "No genres found!");

		return genreEntities.stream().map(
				genreEntity -> modelMapper.map(genreEntity, GenreDto.class)).toList();
	}

	/**
	 * Returns the genre with the given ID.
	 *
	 * @param id the genre ID
	 * @return a GenreDto object
	 * @throws RestApiException if the genre is not found
	 */
	public GenreDto findByID(UUID id) {
		GenreEntity genreEntity = genreRepository.findById(id).orElseThrow(
				() -> new RestApiException(HttpStatus.BAD_REQUEST, "Genre not found!"));

		return modelMapper.map(genreEntity, GenreDto.class);
	}

	/**
	 * Creates a new genre with the given data.
	 *
	 * @param request a GenreDto object containing the genre data
	 * @return the created GenreDto object
	 * @throws RestApiException if a genre with the same name already exists
	 */
	public GenreDto create(GenreDto request) {
		if (genreRepository.existsByName(request.getName()))
			throw new RestApiException(HttpStatus.BAD_REQUEST, "Genre already exists!");

		GenreEntity genreEntity = GenreEntity
				.builder()
				.name(request.getName())
				.description(request.getDescription())
				.build();

		return modelMapper.map(genreRepository.save(genreEntity), GenreDto.class);
	}

	/**
	 * Deletes the genre with the given ID.
	 *
	 * @param id the genre ID
	 * @throws RestApiException if the genre is not found
	 */
	public void deleteByID(UUID id) {
		GenreEntity genreEntity = genreRepository.findById(id).orElseThrow(
				() -> new RestApiException(HttpStatus.BAD_REQUEST, "Genre not found!"));

		genreRepository.delete(genreEntity);
	}

	/**
	 * Updates the genre with the given ID using the data in the OptionalGenreDto
	 * object.
	 *
	 * @param id      the genre ID
	 * @param request an OptionalGenreDto object containing the updated genre data
	 * @return the updated GenreDto object
	 * @throws RestApiException if the genre is not found
	 */
	public GenreDto updateByID(UUID id, OptionalGenreDto request) {
		GenreEntity genreEntity = genreRepository.findById(id).orElseThrow(
				() -> new RestApiException(HttpStatus.BAD_REQUEST, "Genre not found!"));

		if (request.getName() != null)
			genreEntity.setName(request.getName());

		if (request.getDescription() != null)
			genreEntity.setDescription(request.getDescription());

		return modelMapper.map(genreRepository.save(genreEntity), GenreDto.class);
	}

	/**
	 * Searches for genres with the given keyword, sorted and paginated according to
	 * the given parameters.
	 *
	 * @param query     the search keyword
	 * @param page      the page number
	 * @param size      the page size
	 * @param sort      the sort field
	 * @param direction the sort direction
	 * @return a list of GenreDto objects
	 * @throws RestApiException if no genres are found
	 */
	public List<GenreDto> searchByKeyword(String query, Integer page, Integer size, String sort, String direction) {
		Pageable pageable = PageRequest
				.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));

		List<GenreEntity> genreEntities = genreRepository.searchGenreEntities(query, pageable).toList();

		if (genreEntities.isEmpty())
			throw new RestApiException(HttpStatus.NOT_FOUND, "No genres found with given keyword!");

		return genreEntities.stream().map(
				genreEntity -> modelMapper.map(genreEntity, GenreDto.class)).toList();
	}

	/**
	 * Exports all genres to an Excel file and sends it as a response to the client.
	 *
	 * @param response the HttpServletResponse object
	 * @throws RestApiException if an error occurs while exporting the data
	 */
	@Override
	public void exportToExcel(HttpServletResponse response) {
		List<GenreEntity> entityList = genreRepository.findAll();

		try {
			excelUtils.export(response, entityList, EntityOptions.GENRE);
		} catch (IllegalArgumentException e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occurred while exporting data to Excel file: " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occurred while exporting data to Excel file: " + e.getMessage());
		}
	}

	/**
	 * Exports all genres to a CSV file and sends it as a response to the client.
	 *
	 * @param response the HttpServletResponse object
	 * @throws RestApiException if an error occurs while exporting the data
	 */
	@Override
	public void exportToCSV(HttpServletResponse response) {
		List<GenreEntity> entityList = genreRepository.findAll();

		// Converts the entity list to a DTO list
		Iterable<GenreExportDto> dtoList = entityList.stream()
				.map(developerEntity -> modelMapper.map(developerEntity, GenreExportDto.class))
				.collect(Collectors.toList());

		// Gets the class of the DTO
		Class<?> clazz = GenreExportDto.class;

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

		csvUtils.export(response, dtoList, headers, fields);
	}
}
