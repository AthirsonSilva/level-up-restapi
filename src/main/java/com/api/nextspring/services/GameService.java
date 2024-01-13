package com.api.nextspring.services;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.api.nextspring.dto.GameDto;
import com.api.nextspring.dto.optionals.OptionalGameDto;

import jakarta.servlet.http.HttpServletResponse;

/**
 * This interface defines the methods that can be used to interact with the Game
 * entity. It defines methods for retrieving, creating, updating and deleting
 * games.
 * 
 * @author Athirson Silva
 * @see com.api.nextspring.services.impl.GameServiceImpl
 */
public interface GameService {

	/**
	 * Creates a new game with the given data.
	 * 
	 * @param gameDto The data for the new game.
	 * @return The created game.
	 */
	public GameDto create(GameDto gameDto);

	/**
	 * Searches for games that match the given query.
	 * 
	 * @param query     The search query.
	 * @param page      The page number of the search results.
	 * @param size      The number of results per page.
	 * @param sort      The field to sort the results by.
	 * @param direction The direction to sort the results in.
	 * @return A list of games that match the search query.
	 */
	public List<GameDto> searchByKeyword(String query, Pageable pageable);

	/**
	 * Returns all games.
	 * 
	 * @param page      The page number of the search results.
	 * @param size      The number of results per page.
	 * @param sort      The field to sort the results by.
	 * @param direction The direction to sort the results in.
	 * @return A list of all games.
	 */
	public List<GameDto> findAll(Pageable pageable);

	/**
	 * Updates the game with the given ID with the given data.
	 * 
	 * @param id      The ID of the game to update.
	 * @param gameDto The data to update the game with.
	 * @return The updated game.
	 */
	public GameDto updateById(String id, OptionalGameDto gameDto);

	/**
	 * Deletes the game with the given ID.
	 * 
	 * @param id The ID of the game to delete.
	 */
	public void deleteById(String id);

	/**
	 * Returns the game with the given ID.
	 * 
	 * @param id The ID of the game to retrieve.
	 * @return The game with the given ID.
	 */
	public GameDto findByID(String id);

	/**
	 * Uploads a photo for the game with the given ID.
	 * 
	 * @param id   The ID of the game to upload the photo for.
	 * @param file The photo file to upload.
	 * @return The updated game with the new photo.
	 */
	public GameDto uploadPhoto(String id, MultipartFile file);

	/**
	 * Downloads the photo for the game with the given ID.
	 * 
	 * @param id       The ID of the game to download the photo for.
	 * @param response The HTTP response to write the photo data to.
	 */
	public void downloadPhotoByGame(String id, HttpServletResponse response);

	/**
	 * Exports all games to an Excel file and writes it to the given HTTP response.
	 * 
	 * @param response The HTTP response to write the Excel file to.
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
