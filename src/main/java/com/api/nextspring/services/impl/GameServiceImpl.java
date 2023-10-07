package com.api.nextspring.services.impl;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.nextspring.dto.GameDto;
import com.api.nextspring.dto.optionals.OptionalGameDto;
import com.api.nextspring.entity.DeveloperEntity;
import com.api.nextspring.entity.GameEntity;
import com.api.nextspring.entity.GenreEntity;
import com.api.nextspring.enums.EntityOptions;
import com.api.nextspring.exceptions.RestApiException;
import com.api.nextspring.repositories.DeveloperRepository;
import com.api.nextspring.repositories.GameRepository;
import com.api.nextspring.repositories.GenreRepository;
import com.api.nextspring.services.GameService;
import com.api.nextspring.utils.ExcelUtils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class GameServiceImpl implements GameService {
	private final GameRepository gameRepository;
	private final GenreRepository genreRepository;
	private final DeveloperRepository developerRepository;
	private final ModelMapper modelMapper;
	private final ExcelUtils excelUtils;

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

	@Override
	public List<GameDto> searchByKeyword(String query) {
		List<GameEntity> gameEntityList = gameRepository
				.searchGameEntities(query)
				.orElseThrow(
						() -> new RestApiException(
								HttpStatus.NOT_FOUND, "Game with given information was not found!"));

		return gameEntityList.stream().map(game -> modelMapper.map(game, GameDto.class)).toList();
	}

	@Override
	public List<GameDto> findAll() {
		List<GameEntity> gameEntityList = gameRepository.findAll();

		return gameEntityList.stream()
				.map(game -> modelMapper.map(game, GameDto.class)).toList();
	}

	@Override
	public GameDto findByID(UUID id) {
		GameEntity gameEntity = gameRepository
				.findById(id)
				.orElseThrow(
						() -> new RestApiException(
								HttpStatus.NOT_FOUND, "Game with given id was not found!"));

		return modelMapper.map(gameEntity, GameDto.class);
	}

	@Override
	public void deleteById(UUID id) {
		if (gameRepository.existsById(id)) {
			gameRepository.deleteById(id);
		}

		throw new RestApiException(HttpStatus.NOT_FOUND, "Game with given id was not found!");
	}

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

	private GenreEntity getNoGenreEntity() {
		return genreRepository
				.findByName("No Genre")
				.orElse(GenreEntity.builder().name("No Genre").description("No Genre").build());
	}

	private DeveloperEntity getNoDeveloperEntity() {
		return developerRepository
				.findByName("No Developer")
				.orElse(DeveloperEntity.builder().name("No Developer").description("No Developer").build());
	}

	@Override
	public GameDto uploadPhoto(UUID id, MultipartFile file) {
		if (file.isEmpty())
			throw new RestApiException(HttpStatus.BAD_REQUEST, "File is empty!");

		// Create a new Game object and set the path
		GameEntity game = gameRepository.findById(id).orElseThrow(
				() -> new RestApiException(HttpStatus.NOT_FOUND, "Game with given id was not found!"));

		// Define the directory to save the file
		String directory = getFilesPath() + "images/games";
		long seconds = Instant.now().getEpochSecond();
		String photoName = game.getId() + String.valueOf(seconds) + ".png";
		Path path = Paths.get(directory + File.separator + photoName);

		// Save the file in the directory
		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while uploading the file!");
		}

		// Set the path in the Game object
		game.setPhotoPath(path.toString());

		// Save the Game object in the repository
		gameRepository.save(game);

		return modelMapper.map(game, GameDto.class);
	}

	@Override
	public InputStreamResource downloadPhotoByUser(UUID id) {
		GameEntity game = gameRepository.findById(id).orElseThrow(
				() -> new RestApiException(HttpStatus.NOT_FOUND, "Game with given id was not found!"));

		InputStream inputStream = getClass().getResourceAsStream(game.getPhotoPath());

		return inputStream != null
				? new InputStreamResource(inputStream)
				: new InputStreamResource(getClass().getResourceAsStream("https://api.dicebear.com/avatar.svg"));
	}

	@Override
	public void exportToExcel(HttpServletResponse response) {
		List<GameEntity> gameEntityList = gameRepository.findAll();

		try {
			excelUtils.export(response, gameEntityList, EntityOptions.GAME);
		} catch (IllegalArgumentException e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occurred while exporting data to Excel file: " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error occurred while exporting data to Excel file: " + e.getMessage());
		}
	}

	private String getFilesPath() {
		String workingDirectory = System.getProperty("user.dir");
		String directory = workingDirectory + "/src/main/resources/static/";

		return directory;
	}
}
