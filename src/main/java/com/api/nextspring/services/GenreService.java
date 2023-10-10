package com.api.nextspring.services;

import java.util.List;
import java.util.UUID;

import com.api.nextspring.dto.GenreDto;
import com.api.nextspring.dto.optionals.OptionalGenreDto;

import jakarta.servlet.http.HttpServletResponse;

/**
 * This interface defines the methods that can be used to interact with the
 * Genre entity. It defines methods for retrieving, creating, updating and
 * deleting genres.
 * 
 * @author Athirson Silva
 * @see com.api.nextspring.services.impl.GenreServiceImpl
 */
public interface GenreService {

	/**
	 * Returns a list of GenreDto objects, based on the provided pagination, sorting
	 * and filtering parameters.
	 * 
	 * @param page      The page number to retrieve.
	 * @param size      The number of items per page.
	 * @param sort      The field to sort by.
	 * @param direction The direction to sort by (ASC or DESC).
	 * @return A list of GenreDto objects.
	 */
	public List<GenreDto> findAll(Integer page, Integer size, String sort, String direction);

	/**
	 * Returns a GenreDto object with the provided ID.
	 * 
	 * @param id The ID of the GenreDto object to retrieve.
	 * @return A GenreDto object.
	 */
	public GenreDto findByID(UUID id);

	/**
	 * Creates a new GenreDto object.
	 * 
	 * @param request The GenreDto object to create.
	 * @return The created GenreDto object.
	 */
	public GenreDto create(GenreDto request);

	/**
	 * Deletes a GenreDto object with the provided ID.
	 * 
	 * @param id The ID of the GenreDto object to delete.
	 */
	public void deleteByID(UUID id);

	/**
	 * Updates a GenreDto object with the provided ID.
	 * 
	 * @param id      The ID of the GenreDto object to update.
	 * @param request The updated GenreDto object.
	 * @return The updated GenreDto object.
	 */
	public GenreDto updateByID(UUID id, OptionalGenreDto request);

	/**
	 * Searches for GenreDto objects based on the provided keyword and pagination,
	 * sorting and filtering parameters.
	 * 
	 * @param query     The keyword to search for.
	 * @param page      The page number to retrieve.
	 * @param size      The number of items per page.
	 * @param sort      The field to sort by.
	 * @param direction The direction to sort by (ASC or DESC).
	 * @return A list of GenreDto objects.
	 */
	public List<GenreDto> searchByKeyword(String query, Integer page, Integer size, String sort, String direction);

	/**
	 * Exports the list of GenreDto objects to an Excel file and sends it as a
	 * response.
	 * 
	 * @param response The HttpServletResponse object to send the file as a
	 *                 response.
	 */
	public void exportToExcel(HttpServletResponse response);

	/**
	 * Exports the list of GenreDto objects to a CSV file and sends it as a
	 * response.
	 * 
	 * @param response The HttpServletResponse object to send the file as a
	 *                 response.
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
