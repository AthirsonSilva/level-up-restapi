package com.api.nextspring.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.api.nextspring.dto.DeveloperDto;
import com.api.nextspring.dto.GameDto;
import com.api.nextspring.dto.GenreDto;
import com.api.nextspring.dto.UserDto;
import com.api.nextspring.entity.DeveloperEntity;
import com.api.nextspring.entity.GameEntity;
import com.api.nextspring.entity.GenreEntity;
import com.api.nextspring.entity.UserEntity;
import com.api.nextspring.enums.EntityOptions;
import com.api.nextspring.exceptions.RestApiException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This class provides utility methods for working with Excel files. It includes
 * methods for creating and populating
 * Excel sheets with data from different entity types. The class uses
 * ModelMapper to map entity objects to DTO objects
 * before writing them to the Excel sheet.
 * 
 * @param workbook    the workbook of the excel file
 * @param sheet       the sheet of the excel file
 * @param modelMapper the model mapper that will be used to map entity objects
 *                    to
 * 
 * @see XSSFWorkbook
 * @see XSSFSheet
 * 
 * @author Athirson Silva
 */
@Service
public class ExcelUtils {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private final ModelMapper modelMapper;

	public ExcelUtils(ModelMapper modelMapper) {
		this.workbook = new XSSFWorkbook();
		this.sheet = workbook.createSheet("Users");
		this.modelMapper = modelMapper;
	}

	/**
	 * Writes the header line of the excel file cells
	 * 
	 * @param entity the entity type that will be exported to excel which will be
	 *               used to write the header line
	 */
	private void writeHeaderLine(EntityOptions entity) {
		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

		Class<?> clazz = getEntityClassType(entity);
		Field[] fields = clazz.getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			createCell(row, i, fields[i].getName(), style);
		}
	}

	/**
	 * Returns the class type of the entity that will be exported to excel
	 * 
	 * @param entity the entity type that will be exported to excel
	 * @return the class type of the entity that will be exported to excel
	 */
	private Class<?> getEntityClassType(EntityOptions entity) {
		return switch (entity) {
			case GAME -> GameDto.class;
			case GENRE -> GenreDto.class;
			case DEVELOPER -> DeveloperDto.class;
			case USER -> UserDto.class;
			default -> throw new RestApiException(HttpStatus.BAD_REQUEST, "Invalid entity!");
		};
	}

	/**
	 * Creates a cell in the excel file with the given value and style
	 * 
	 * @param row         the row of the excel file
	 * @param columnCount the column of the excel file
	 * @param value       the value of the excel file cell
	 * @param style       the style of the excel file cells
	 */
	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);

		if (value instanceof Integer)
			cell.setCellValue((Integer) value);

		else if (value instanceof Boolean)
			cell.setCellValue((Boolean) value);

		else
			cell.setCellValue(value.toString());

		cell.setCellStyle(style);
	}

	/**
	 * Writes the data lines of the excel file cells
	 * 
	 * @return the style of the excel file cells which will be used to write the
	 *         data
	 * @throws IllegalArgumentException if the entityList is null
	 * @throws IllegalAccessException   if the entityList is inaccessible
	 */
	private CellStyle writeDataLines() throws IllegalArgumentException, IllegalAccessException {
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(12);
		style.setFont(font);

		return style;
	}

	/**
	 * Populates the sheet with the data of the game entities
	 * 
	 * @param <T>        the generic type of the entity
	 * @param entityList the list of entities that will be exported to excel
	 * @param style      the style of the excel file cells
	 * @throws IllegalAccessException if the entityList is inaccessible
	 */
	private <T> void populateGameDtoSheet(List<T> entityList, CellStyle style) throws IllegalAccessException {
		int rowCount = 1;
		List<GameDto> dtoList = entityList.stream().map(entity -> modelMapper.map(entity, GameDto.class)).toList();

		for (var entity : dtoList) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			Class<?> clazz = entity.getClass();
			Field[] fields = clazz.getDeclaredFields();

			for (Field field : fields) {
				field.setAccessible(true);
				createCell(row, columnCount++, field.get(entity), style);
			}
		}
	}

	/**
	 * Populates the sheet with the data of the genre entities
	 * 
	 * @param <T>        the generic type of the entity
	 * @param entityList the list of entities that will be exported to excel
	 * @param style      the style of the excel file cells
	 * @throws IllegalAccessException if the entityList is inaccessible
	 */
	private <T> void populateGenreDtoSheet(List<T> entityList, CellStyle style) throws IllegalAccessException {
		int rowCount = 1;
		List<GenreDto> dtoList = entityList.stream().map(entity -> modelMapper.map(entity, GenreDto.class)).toList();

		for (var entity : dtoList) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			Class<?> clazz = entity.getClass();
			Field[] fields = clazz.getDeclaredFields();

			for (Field field : fields) {
				field.setAccessible(true);
				createCell(row, columnCount++, field.get(entity), style);
			}
		}
	}

	/**
	 * Populates the sheet with the data of the developer entities
	 * 
	 * @param <T>        the generic type of the entity
	 * @param entityList the list of entities that will be exported to excel
	 * @param style      the style of the excel file cells
	 * @throws IllegalAccessException if the entityList is inaccessible
	 */
	private <T> void populateDeveloperDtoSheet(List<T> entityList, CellStyle style) throws IllegalAccessException {
		int rowCount = 1;
		List<DeveloperDto> dtoList = entityList.stream().map(entity -> modelMapper.map(entity, DeveloperDto.class))
				.toList();

		for (var entity : dtoList) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			Class<?> clazz = entity.getClass();
			Field[] fields = clazz.getDeclaredFields();

			for (Field field : fields) {
				field.setAccessible(true);
				createCell(row, columnCount++, field.get(entity), style);
			}
		}
	}

	/**
	 * Populates the sheet with the data of the user entities
	 * 
	 * @param <T>        the generic type of the entity
	 * @param entityList the list of entities that will be exported to excel
	 * @param style      the style of the excel file cells
	 * @throws IllegalAccessException if the entityList is inaccessible
	 */
	private <T> void populateUserDtoSheet(List<T> entityList, CellStyle style) throws IllegalAccessException {
		int rowCount = 1;
		List<UserDto> dtoList = entityList.stream().map(entity -> modelMapper.map(entity, UserDto.class)).toList();

		for (var entity : dtoList) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			Class<?> clazz = entity.getClass();
			Field[] fields = clazz.getDeclaredFields();

			for (Field field : fields) {
				field.setAccessible(true);
				createCell(row, columnCount++, field.get(entity), style);
			}
		}
	}

	/**
	 * Selects the sheet that will be populated with the data of the entity
	 * 
	 * @param <T>        the generic type of the entity
	 * @param entityList the list of entities that will be exported to excel
	 * @param entity     the entity type that will be exported to excel
	 * @throws IllegalAccessException if the entityList is inaccessible
	 */
	@SuppressWarnings("unchecked")
	private <T> void selectAndPopulateSheet(List<T> entityList, EntityOptions entity) throws IllegalAccessException {
		switch (entity) {
			case GAME -> populateGameDtoSheet((List<GameEntity>) entityList, writeDataLines());
			case GENRE -> populateGenreDtoSheet((List<GenreEntity>) entityList, writeDataLines());
			case DEVELOPER -> populateDeveloperDtoSheet((List<DeveloperEntity>) entityList, writeDataLines());
			case USER -> populateUserDtoSheet((List<UserEntity>) entityList, writeDataLines());
			default -> throw new RestApiException(HttpStatus.BAD_REQUEST, "Invalid entity!");
		}
	}

	/**
	 * Exports the data of the entity to excel and writes it to the response object
	 * 
	 * @param <T>
	 * @param response   the HttpServletResponse object that is used to write the
	 *                   excel file
	 * @param entityList the list of entities that will be exported to excel
	 * @param entity     the entity type that will be exported to excel
	 * @throws IllegalArgumentException if the entityList is null
	 * @throws IllegalAccessException   if the entityList is inaccessible
	 */
	public <T> void export(HttpServletResponse response, List<T> entityList, EntityOptions entity)
			throws IllegalArgumentException, IllegalAccessException {
		writeHeaderLine(entity);
		writeDataLines();
		selectAndPopulateSheet(entityList, entity);

		try (ServletOutputStream outputStream = response.getOutputStream();) {
			workbook.write(outputStream);
			workbook.close();
		} catch (IOException e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occurred while exporting data to Excel file: " + e.getMessage());
		}
	}
}
