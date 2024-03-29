package com.api.levelup.services.impl;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.api.levelup.dto.GenreDto;
import com.api.levelup.dto.export.GenreExportDto;
import com.api.levelup.dto.optionals.OptionalGenreDto;
import com.api.levelup.dto.request.GenreRequestDto;
import com.api.levelup.entity.GenreEntity;
import com.api.levelup.enums.EntityOptions;
import com.api.levelup.exceptions.RestApiException;
import com.api.levelup.repositories.GenreRepository;
import com.api.levelup.services.GenreService;
import com.api.levelup.utils.CsvExporter;
import com.api.levelup.utils.ExcelExporter;
import com.api.levelup.utils.ExporterDtoMapper;
import com.api.levelup.utils.PdfExporter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * This class implements the GenreService interface and provides the
 * implementation for all its methods.
 * It uses GenreRepository, ModelMapper, and excelExporter to perform CRUD
 * operations on GenreEntity objects.
 * 
 * @author Athirson Silva
 * @implNote This class implements the UserService interface and provides the
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class GenreServiceImpl implements GenreService {
	private final GenreRepository genreRepository;
	private final ModelMapper modelMapper;
	private final ExporterDtoMapper mappingCustomizer;
	private final ExcelExporter excelExporter;
	private final CsvExporter csvExporter;
	private final PdfExporter pdfExporter;

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
	@Override
	@Cacheable(value = "genres")
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
	@Override
	public GenreDto findByID(String id) {
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
	@Override
	public GenreDto create(GenreRequestDto request) {
		if (genreRepository.existsByName(request.getName()))
			throw new RestApiException(HttpStatus.BAD_REQUEST, "Genre already exists!");

		GenreEntity genreEntity = GenreEntity
				.builder()
				.name(request.getName())
				.description(request.getDescription())
				.games(Collections.emptyList())
				.build();

		return modelMapper.map(genreRepository.save(genreEntity), GenreDto.class);
	}

	/**
	 * Deletes the genre with the given ID.
	 *
	 * @param id the genre ID
	 * @throws RestApiException if the genre is not found
	 */
	@Override
	public void deleteByID(String id) {
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
	@Override
	public GenreDto updateByID(String id, OptionalGenreDto request) {
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
	@Override
	@Cacheable(value = "genres", key = "#query")
	public List<GenreDto> searchByKeyword(String query, Pageable pageable) {
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

		List<GenreExportDto> dtoList = entityList.stream()
				.map(developerEntity -> modelMapper.map(developerEntity, GenreExportDto.class))
				.collect(Collectors.toList());

		try {
			excelExporter.export(response, dtoList, EntityOptions.GENRE);
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

		csvExporter.export(response, dtoList, headers, fields);
	}

	/**
	 * Exports all genres to a PDF file and sends it as a response to the client.
	 *
	 * @param response the HTTP servlet response to send the PDF file to
	 */
	public void exportToPDF(HttpServletResponse response) {
		List<GenreEntity> entityList = genreRepository.findAll();

		List<GenreExportDto> dtoList = entityList.stream()
				.map(entity -> mappingCustomizer.toGenreExportDto(entity))
				.collect(Collectors.toList());

		log.info("Exporting {} rows to PDF file...", dtoList.size());

		pdfExporter.export(response, dtoList, EntityOptions.GENRE);
	}
}
