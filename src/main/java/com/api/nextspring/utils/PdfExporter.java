package com.api.nextspring.utils;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.api.nextspring.dto.export.DeveloperExportDto;
import com.api.nextspring.dto.export.GameExportDto;
import com.api.nextspring.dto.export.GenreExportDto;
import com.api.nextspring.dto.export.UserExportDto;
import com.api.nextspring.enums.EntityOptions;
import com.api.nextspring.exceptions.RestApiException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

/**
 * This class provides functionality to export data to PDF format.
 * It includes methods to export a list of entities to PDF, with the option to
 * specify the entity type.
 * The exported PDF includes a table with the data of the entities.
 * 
 * @see Document
 * @see PdfPTable
 * @see PdfPCell
 * @see PdfWriter
 * @see Paragraph
 * @see Font
 * @see FontFactory
 * 
 * @author Athirson Silva
 */
@Service
@Log4j2
public class PdfExporter {

	/**
	 * Returns the class type of the entity that will be exported to excel
	 * 
	 * @param entity the entity type that will be exported to excel
	 * @return the class type of the entity that will be exported to excel
	 */
	private Class<?> getEntityClassType(EntityOptions entity) {
		return switch (entity) {
			case GAME -> GameExportDto.class;
			case GENRE -> GenreExportDto.class;
			case DEVELOPER -> DeveloperExportDto.class;
			case USER -> UserExportDto.class;
			default -> throw new RestApiException(HttpStatus.BAD_REQUEST, "Invalid entity!");
		};
	}

	/**
	 * Writes the table header for the given entity type
	 * 
	 * @param table      the PDF table to write the header to
	 * @param entityType the type of entity to write the header for
	 */
	private void writeTableHeader(PdfPTable table, EntityOptions entityType) {
		// Creates a PDF cell
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.DARK_GRAY);
		cell.setPadding(5);

		// Creates a font for the table header
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);

		// Gets the class type of the entity
		Class<?> entityClass = getEntityClassType(entityType);
		Field[] fields = entityClass.getDeclaredFields();

		// Writes the header for each field of the entity
		for (Field field : fields) {
			field.setAccessible(true);

			log.info(field.getName());

			cell.setPhrase(new Paragraph(field.getName(), font));
			table.addCell(cell);
		}
	}

	/**
	 * Writes the table data for the given entity list
	 * 
	 * @param table      the PDF table to write the data to
	 * @param entityList the list of entities to write the data for
	 */
	private <T> void writeTableData(PdfPTable table, List<T> entityList) {
		for (T entity : entityList) {
			// Gets the class type of the entity
			Class<? extends Object> entityClass = entity.getClass();
			Field[] fields = entityClass.getDeclaredFields();

			// Writes the data for each field of the entity
			for (Field field : fields) {
				field.setAccessible(true);

				try {
					// Gets the value of the field
					Object value = field.get(entity);

					// If the value is null, set it to N/A
					if (value == null || value.toString().isEmpty()) {
						value = "N/A";
					}

					log.info(value.toString());

					table.addCell(value.toString());
				} catch (IllegalArgumentException | IllegalAccessException e) {
					log.error("Error while exporting data to PDF: {}", e.getMessage());

					throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while exporting data to PDF");
				}
			}
		}
	}

	/**
	 * Exports the given entity list to a PDF file and writes it to the response
	 * 
	 * @param response   the HTTP response to write the PDF file to
	 * @param entityList the list of entities to export to PDF
	 * @param entityType the type of entity to export to PDF
	 * @throws DocumentException if there is an error creating the PDF document
	 * @throws IOException       if there is an error writing the PDF file to the
	 *                           response
	 */
	public <T> void export(HttpServletResponse response, List<T> entityList, EntityOptions entityType) {
		try {

			log.info("Exporting data to PDF: {}", entityType);

			// Creates a PDF document with A2 size
			Document document = new Document(PageSize.A2);

			log.info("Document size: {}", document.getPageSize());

			// Writes the PDF document to the response
			PdfWriter.getInstance(document, (OutputStream) response.getOutputStream());

			document.open();

			// Creates a font for the table title
			Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			font.setSize(18);
			font.setColor(Color.DARK_GRAY);

			// Creates the table title
			String tableTitle = String.format("List of %ss", entityType.name().toLowerCase());

			// Creates a paragraph with the table title
			Paragraph paragraph = new Paragraph(tableTitle, font);
			paragraph.setAlignment(Paragraph.ALIGN_CENTER);

			// Adds the paragraph to the document
			document.add(paragraph);

			// Gets the class type of the entity
			Class<? extends Object> entityClass = getEntityClassType(entityType);
			Field[] fields = entityClass.getDeclaredFields();

			// Creates a PDF table with the number of fields of the entity
			PdfPTable table = new PdfPTable(fields.length);

			// Sets the table width to 100% of the page
			table.setWidthPercentage(100f);

			// Sets the widths of the table columns
			float[] widths = new float[fields.length];

			for (int i = 0; i < fields.length; i++) {
				widths[i] = 2f;
			}

			table.setWidths(widths);
			table.setSpacingBefore(10);

			// Writes the table header and data
			writeTableHeader(table, entityType);
			writeTableData(table, entityList);

			// Adds the table to the document
			document.add(table);

			document.close();
		} catch (IOException | DocumentException | IllegalArgumentException e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occurred while writing PDF file to response: " + e.getMessage());
		}
	}
}
