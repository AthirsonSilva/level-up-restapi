package com.api.levelup.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.api.levelup.exceptions.RestApiException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

/**
 * This class provides utility methods for exporting data to CSV format.
 * It contains two overloaded methods for exporting data to CSV format.
 */
@Service
@Log4j2
public class CsvExporter {

	/**
	 * Exports a list of objects to a CSV file and writes the file to the
	 * HttpServletResponse object.
	 * 
	 * @param response   the HttpServletResponse object to write the CSV file to
	 * @param entityList the list of objects to export to the CSV file
	 * @param headers    an array of header strings to use for the CSV file
	 * @param fields     an array of field names to use for the CSV file
	 * @throws RestApiException if an error occurs while exporting the data to the
	 *                          CSV file
	 */
	public <T> void export(HttpServletResponse response, Iterable<T> entityList, String[] headers, String[] fields) {
		// Create a CSV printer instance with default settings
		try (CSVPrinter csvPrinter = new CSVPrinter(response.getWriter(), CSVFormat.DEFAULT)) {
			// Create CSV file header using the array of headers passed as parameter
			csvPrinter.printRecord((Object[]) headers);

			// Iterate over the list of entities passed as parameter
			for (T entity : entityList) {
				// Create a list of objects to write to the CSV file
				List<Object> record = new ArrayList<>();

				// Iterate over the array of fields passed as parameter and get the value of the
				// field for the current entity
				for (String field : fields) {
					// Get the declared field of the entity class
					Field declaredField = entity.getClass().getDeclaredField(field);
					declaredField.setAccessible(true);

					Object value = declaredField.get(entity);

					if (value == null || value.toString().isEmpty()) {
						value = "N/A";
					}

					log.info("Cell value: {}", value.toString());

					// Add the value of the field to the list of objects to write to the CSV file
					record.add(value);
				}

				// Write the list of objects to the CSV file
				csvPrinter.printRecord(record);
			}
		} catch (IOException | NoSuchFieldException | IllegalAccessException e) {
			log.error("Error occurred while exporting data to CSV file: " + e.getMessage());

			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occurred while exporting data to CSV file: " + e.getMessage());
		}
	}

}
