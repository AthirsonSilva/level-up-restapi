package com.api.nextspring.services.impl;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.api.nextspring.dto.GameDto;
import com.api.nextspring.dto.export.GameExportDto;
import com.api.nextspring.dto.optionals.OptionalGameDto;
import com.api.nextspring.entity.DeveloperEntity;
import com.api.nextspring.entity.GameEntity;
import com.api.nextspring.entity.GenreEntity;
import com.api.nextspring.enums.EntityOptions;
import com.api.nextspring.exceptions.RestApiException;
import com.api.nextspring.repositories.DeveloperRepository;
import com.api.nextspring.repositories.GameRepository;
import com.api.nextspring.repositories.GenreRepository;
import com.api.nextspring.repositories.custom.CustomGameRepository;
import com.api.nextspring.services.GameService;
import com.api.nextspring.utils.CsvExporter;
import com.api.nextspring.utils.ExcelExporter;
import com.api.nextspring.utils.FileManager;
import com.api.nextspring.utils.PdfExporter;
import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * This class implements the GameService interface and provides the
 * implementation for the CRUD operations
 * for the Game entity. It also provides methods for exporting the game data to
 * an Excel file and downloading the photo of a game.
 * 
 * @author Athirson Silva
 * @implNote This class implements the UserService interface and provides the
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class GameServiceImpl implements GameService {

	// Dependencies
	private final GameRepository gameRepository;
	private final CustomGameRepository customGameRepository;
	private final GenreRepository genreRepository;
	private final DeveloperRepository developerRepository;
	private final ModelMapper modelMapper;
	private final ExcelExporter excelExporter;
	private final CsvExporter csvExporter;
	private final PdfExporter pdfExporter;
	private final FileManager fileManager;

	/**
	 * Creates a new Game entity from the provided GameDto object.
	 * 
	 * @param gameDto The GameDto object to create the entity from.
	 * @return The created GameDto object.
	 * @throws RestApiException If a game with the same name already exists or if
	 *                          the genre or developer ID is invalid.
	 */
	@Override
	public GameDto create(GameDto gameDto) {
		log.info("Game object received: " + gameDto.toString());

		if (gameDto.getGenreId() == null || !genreRepository.existsById(gameDto.getGenreId())) {
			gameDto.setGenreId(getNoGenreEntity().getId());
		} else if (gameDto.getDeveloperId() == null
				|| !developerRepository.existsById(gameDto.getDeveloperId())) {
			gameDto.setDeveloperId(getNoDeveloperEntity().getId());
		}

		GenreEntity genreEntity = genreRepository.findById(gameDto.getGenreId()).orElse(getNoGenreEntity());
		DeveloperEntity developerEntity = developerRepository.findById(gameDto.getDeveloperId())
				.orElse(getNoDeveloperEntity());

		genreRepository.save(genreEntity);

		GameEntity gameEntity = GameEntity.builder()
				.description(gameDto.getDescription())
				.year(gameDto.getYear())
				.grade(gameDto.getGrade().name())
				.genre(genreEntity)
				.developer(developerEntity)
				.build();

		if (gameRepository.existsByName(gameDto.getName()))
			throw new RestApiException(HttpStatus.BAD_REQUEST, "Game with given name already exists!");
		else
			gameEntity.setName(gameDto.getName());

		log.info("Saving game: " + gameEntity.toString());

		GameEntity savedGame = gameRepository.save(gameEntity);

		return modelMapper.map(savedGame, GameDto.class);
	}

	/**
	 * Searches for Game entities that match the provided keyword.
	 * 
	 * @param query     The keyword to search for.
	 * @param page      The page number of the search results.
	 * @param size      The number of results per page.
	 * @param sort      The field to sort the results by.
	 * @param direction The direction to sort the results in.
	 * @return A list of GameDto objects that match the search query.
	 * @throws RestApiException If no games are found with the given keyword.
	 */
	@Override
	public List<GameDto> searchByKeyword(String query, Integer page, Integer size, String sort, String direction) {
		Pageable pageable = PageRequest
				.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));

		List<GameEntity> gameEntityList = customGameRepository
				.findAllByFilter(query, pageable)
				.toList();

		if (gameEntityList.isEmpty())
			throw new RestApiException(HttpStatus.NOT_FOUND, "No games found with given keyword!");

		return gameEntityList.stream().map(game -> modelMapper.map(game, GameDto.class)).toList();
	}

	/**
	 * Retrieves all Game entities.
	 * 
	 * @param page      The page number of the search results.
	 * @param size      The number of results per page.
	 * @param sort      The field to sort the results by.
	 * @param direction The direction to sort the results in.
	 * @return A list of all GameDto objects.
	 * @throws RestApiException If no games are found.
	 */
	@Override
	public List<GameDto> findAll(Integer page, Integer size, String sort, String direction) {
		Pageable pageable = PageRequest
				.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));

		List<GameEntity> gameEntityList = customGameRepository.findAll(pageable).toList();

		if (gameEntityList.isEmpty())
			throw new RestApiException(HttpStatus.NOT_FOUND, "No games found!");

		return gameEntityList.stream()
				.map(game -> modelMapper.map(game, GameDto.class)).toList();
	}

	/**
	 * Retrieves a Game entity by its ID.
	 * 
	 * @param id The ID of the Game entity to retrieve.
	 * @return The GameDto object that matches the provided ID.
	 * @throws RestApiException If no game is found with the provided ID.
	 */
	@Override
	public GameDto findByID(UUID id) {
		GameEntity gameEntity = gameRepository
				.findById(id)
				.orElseThrow(
						() -> new RestApiException(
								HttpStatus.NOT_FOUND, "Game with given id was not found!"));

		return modelMapper.map(gameEntity, GameDto.class);
	}

	/**
	 * Deletes a Game entity by its ID.
	 * 
	 * @param id The ID of the Game entity to delete.
	 * @throws RestApiException If no game is found with the provided ID.
	 */
	@Override
	public void deleteById(UUID id) {
		if (gameRepository.existsById(id)) {
			gameRepository.deleteById(id);
		}

		throw new RestApiException(HttpStatus.NOT_FOUND, "Game with given id was not found!");
	}

	/**
	 * Updates a Game entity by its ID with the provided OptionalGameDto object.
	 * 
	 * @param id      The ID of the Game entity to update.
	 * @param request The OptionalGameDto object containing the fields to update.
	 * @return The updated GameDto object.
	 * @throws RestApiException If no game is found with the provided ID.
	 */
	@Override
	public GameDto updateById(UUID id, OptionalGameDto request) {
		GameEntity gameEntity = gameRepository
				.findById(id)
				.orElseThrow(
						() -> new RestApiException(
								HttpStatus.NOT_FOUND, "Game with given id was not found!"));

		if (request.getName() != null)
			gameEntity.setName(request.getName());

		if (request.getDescription() != null)
			gameEntity.setDescription(request.getDescription());

		if (request.getGrade() != null)
			gameEntity.setGrade(request.getGrade().name());

		if (request.getGenreId() != null)
			gameEntity.setGenre(
					genreRepository.findById(request.getGenreId()).orElse(getNoGenreEntity()));

		if (request.getDeveloperId() != null)
			gameEntity.setDeveloper(
					developerRepository.findById(request.getDeveloperId()).orElse(getNoDeveloperEntity()));

		return modelMapper.map(gameRepository.save(gameEntity), GameDto.class);
	}

	/**
	 * Exports all Game entities to an Excel file and sends it as a response.
	 * 
	 * @param response The HttpServletResponse object to send the file as a
	 *                 response.
	 * @throws RestApiException If an error occurs while exporting the data to the
	 *                          Excel file.
	 */
	@Override
	public void exportToExcel(HttpServletResponse response) {
		List<GameEntity> entityList = gameRepository.findAll();

		List<GameExportDto> dtoList = entityList.stream()
				.map(gameEntity -> modelMapper.map(gameEntity, GameExportDto.class))
				.collect(Collectors.toList());

		try {
			excelExporter.export(response, dtoList, EntityOptions.GAME);
		} catch (IllegalArgumentException e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occurred while exporting data to Excel file: " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occurred while exporting data to Excel file: " + e.getMessage());
		}
	}

	/**
	 * Downloads the photo of a Game entity by its ID and sends it as a response.
	 * 
	 * @param id       The ID of the Game entity to download the photo of.
	 * @param response The HttpServletResponse object to send the photo as a
	 *                 response.
	 * @throws RestApiException If no game is found with the provided ID.
	 */
	@Override
	public void downloadPhotoByGame(UUID id, HttpServletResponse response) {
		GameEntity entity = gameRepository
				.findById(id)
				.orElseThrow(
						() -> new RestApiException(
								HttpStatus.NOT_FOUND, "Game with given id was not found!"));

		fileManager.getPhoto(entity.getPhotoPath(), response);
	}

	@Override
	public GameDto uploadPhoto(UUID id, MultipartFile file) {
		GameEntity entity = gameRepository
				.findById(id)
				.orElseThrow(
						() -> new RestApiException(
								HttpStatus.NOT_FOUND, "Game with given id was not found!"));

		String filePath = fileManager.savePhoto(id, file);

		entity.setPhotoPath(filePath);

		GameEntity save = gameRepository.save(entity);

		return modelMapper.map(save, GameDto.class);
	}

	/**
	 * Retrieves the "No Genre" GenreEntity object.
	 * 
	 * @return The "No Genre" GenreEntity object.
	 */
	private GenreEntity getNoGenreEntity() {
		return genreRepository
				.findByName("No Genre")
				.orElse(GenreEntity.builder().name("No Genre").description("No Genre").build());
	}

	/**
	 * Retrieves the "No Developer" DeveloperEntity object.
	 * 
	 * @return The "No Developer" DeveloperEntity object.
	 */
	private DeveloperEntity getNoDeveloperEntity() {
		return developerRepository
				.findByName("No Developer")
				.orElse(DeveloperEntity.builder().name("No Developer").description("No Developer").build());
	}

	@Override
	public void exportToCSV(HttpServletResponse response) {
		List<GameEntity> entityList = gameRepository.findAll();

		// Converts the entity list to a DTO list
		Iterable<GameExportDto> dtoList = entityList.stream()
				.map(developerEntity -> modelMapper.map(developerEntity, GameExportDto.class))
				.collect(Collectors.toList());

		// Gets the class of the DTO
		Class<?> clazz = GameExportDto.class;

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

	public void exportToPDF(HttpServletResponse response) {
		List<GameEntity> entityList = gameRepository.findAll();

		List<GameExportDto> dtoList = entityList.stream()
				.map(entity -> modelMapper.map(entity, GameExportDto.class))
				.collect(Collectors.toList());

		log.info("Exporting {} rows to PDF file...", dtoList.size());

		try {
			pdfExporter.export(response, dtoList, EntityOptions.GAME);
		} catch (IllegalArgumentException e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occurred while exporting data to PDF file: " + e.getMessage());
		} catch (DocumentException e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occurred while exporting data to PDF file: " + e.getMessage());
		} catch (IOException e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occurred while exporting data to PDF file: " + e.getMessage());
		}
	}
}
