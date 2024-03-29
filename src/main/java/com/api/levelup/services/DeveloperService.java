package com.api.levelup.services;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.api.levelup.dto.DeveloperDto;
import com.api.levelup.dto.optionals.OptionalDeveloperDto;
import com.api.levelup.dto.request.DeveloperRequestDto;

import jakarta.servlet.http.HttpServletResponse;

/**
 * The DeveloperService interface provides methods for creating, reading,
 * updating, and deleting developer data
 * from a database, as well as searching and exporting data to Excel.
 * 
 * @author Athirson Silva
 * @see com.api.levelup.services.impl.DeveloperServiceImpl
 */
public interface DeveloperService {
	/**
	 * Creates a developer in the database and returns the developer data created
	 * 
	 * @param developerDto the developer data to be created in the database
	 * @return a dto containing the developer data created
	 */
	public DeveloperDto create(DeveloperRequestDto developerDto);

	/**
	 * Finds all developers in the database and returns a list of developers data
	 * sorted and paginated
	 * 
	 * @param page      the page number of the developers to be returned
	 * @param size      the number of developers to be returned
	 * @param sort      the field to be sorted
	 * @param direction the direction of the sort
	 * @return a list of developers data sorted and paginated
	 */
	public List<DeveloperDto> findAll(Pageable pageable);

	/**
	 * Finds a developer in the database by id and returns a dto containing the
	 * developer data found
	 * 
	 * @param id the id of the developer to be returned
	 * @return a dto containing the developer data found
	 */
	public DeveloperDto findByID(String id);

	/**
	 * Find and update a developer in the database by id and returns a dto
	 * containing the developer data updated
	 * 
	 * @param id           the id of the developer to be updated
	 * @param developerDto the developer data to be updated
	 * @return a dto containing the developer data updated
	 */
	public DeveloperDto updateByID(String id, OptionalDeveloperDto developerDto);

	/**
	 * Find and delete a developer in the database by id
	 * 
	 * @param id the id of the developer to be deleted
	 */
	public void deleteByID(String id);

	/**
	 * Searches for developers in the database by keyword and returns a list
	 * paginated and sorted
	 * 
	 * @param query     the query containing the keyword to be searched
	 * @param page      the page number of the developers to be returned
	 * @param size      the number of developers to be returned
	 * @param sort      the field to be sorted
	 * @param direction the direction of the sort
	 * @return a list of developers data sorted and paginated
	 */
	public List<DeveloperDto> search(String query, Pageable pageable);

	/**
	 * Exports the developers data to an Excel file
	 * 
	 * @param response the response object that will be used to download the file to
	 *                 the user
	 */
	public void exportToExcel(HttpServletResponse response);

	/**
	 * Exports the developers data to a CSV file
	 * 
	 * @param response the response object that will be used to download the file to
	 *                 the user
	 */
	public void exportToCSV(HttpServletResponse response);

	/**
	 * Exports the developers data to a PDF file
	 * 
	 * @param response the response object that will be used to download the file to
	 *                 the user
	 */
	public void exportToPDF(HttpServletResponse response);
}
