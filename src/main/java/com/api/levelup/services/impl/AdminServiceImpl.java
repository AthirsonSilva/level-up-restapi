package com.api.levelup.services.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.api.levelup.dto.export.UserExportDto;
import com.api.levelup.entity.UserEntity;
import com.api.levelup.enums.EntityOptions;
import com.api.levelup.exceptions.RestApiException;
import com.api.levelup.repositories.UserRepository;
import com.api.levelup.services.AdminService;
import com.api.levelup.utils.CsvExporter;
import com.api.levelup.utils.ExcelExporter;
import com.api.levelup.utils.ExporterDtoMapper;
import com.api.levelup.utils.PdfExporter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * The implementation of the AdminService interface.
 * Provides methods for exporting users from the database to CSV, Excel and PDF
 * files.
 * 
 * @see AdminService
 * @see UserRepository
 * @see ModelMapper
 * @see ExporterDtoMapper
 * @see ExcelExporter
 * @see CsvExporter
 * @see PdfExporter
 * 
 * @implNote This class uses the ModelMapper library to map the UserEntity to
 *           the
 * 
 * @author Athirson Silva
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class AdminServiceImpl implements AdminService {

	private final UserRepository userRepository;
	private final ExporterDtoMapper exporterDtoMapper;
	private final ExcelExporter excelExporter;
	private final CsvExporter csvExporter;
	private final PdfExporter pdfExporter;

	/**
	 * Exports all the users from the database to a CSV file and sends it as a
	 * response to the client.
	 * If there are no users in the database, a RestApiException with status code
	 * NOT_FOUND is thrown.
	 *
	 * @param response the HttpServletResponse object used to send the CSV file as a
	 *                 response to the client
	 */
	@Override
	public void exportToCSV(HttpServletResponse response) {
		// Gets all the users from the database
		List<UserEntity> entityList = userRepository.findAll();

		log.info("Exporting {} rows to CSV file...", entityList.size());

		// Checks if there are users in the database
		if (entityList.isEmpty()) {
			log.error("There are no users in the database");

			throw new RestApiException(HttpStatus.NOT_FOUND, "There are no users in the database");
		}

		// Maps the usersDto to the UserExportDto
		List<UserExportDto> formattedDtoList = entityList.stream()
				.map(entity -> exporterDtoMapper.toUserExportDto(entity)).toList();

		Class<?> clazz = UserExportDto.class;

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

		log.info("Exporting {} rows to CSV file...", formattedDtoList.size());

		csvExporter.export(response, formattedDtoList, headers, fields);

	}

	/**
	 * Exports all users in the database to an Excel file and sends it as a
	 * response to the client.
	 *
	 * @param response the HttpServletResponse object representing the response to
	 *                 send to the client.
	 * @throws RestApiException if an error occurs while exporting the data to the
	 *                          Excel file.
	 */
	@Override
	public void exportToExcel(HttpServletResponse response) {
		List<UserEntity> entityList = userRepository.findAll();

		// Checks if there are users in the database
		if (entityList.isEmpty()) {
			log.error("There are no users in the database");

			throw new RestApiException(HttpStatus.NOT_FOUND, "There are no users in the database");
		}

		List<UserExportDto> dtoList = entityList.stream()
				.map(entity -> exporterDtoMapper.toUserExportDto(entity))
				.toList();

		log.info("Exporting {} rows to Excel file...", dtoList.size());

		try {
			excelExporter.export(response, dtoList, EntityOptions.USER);
		} catch (IllegalArgumentException e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occurred while exporting data to Excel file: " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occurred while exporting data to Excel file: " + e.getMessage());
		}
	}

	/**
	 * Exports all users in the database to a PDF file and sends it as a response to
	 * the client.
	 *
	 * @param response the HTTP servlet response to send the PDF file to
	 * @throws RestApiException if there are no users in the database
	 */
	public void exportToPDF(HttpServletResponse response) {
		List<UserEntity> entityList = userRepository.findAll();

		if (entityList.isEmpty()) {
			log.error("There are no users in the database");

			throw new RestApiException(HttpStatus.NOT_FOUND, "There are no users in the database");
		}

		List<UserExportDto> dtoList = entityList.stream()
				.map(entity -> exporterDtoMapper.toUserExportDto(entity))
				.collect(Collectors.toList());

		log.info("Exporting {} rows to PDF file...", dtoList.size());

		pdfExporter.export(response, dtoList, EntityOptions.USER);
	}

}
