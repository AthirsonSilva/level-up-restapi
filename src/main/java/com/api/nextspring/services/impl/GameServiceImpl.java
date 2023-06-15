package com.api.nextspring.services.impl;

import com.api.nextspring.entity.DeveloperEntity;
import com.api.nextspring.entity.GameEntity;
import com.api.nextspring.entity.GenreEntity;
import com.api.nextspring.exceptions.RestApiException;
import com.api.nextspring.payload.GameDto;
import com.api.nextspring.payload.optionals.OptionalGameDto;
import com.api.nextspring.repositories.DeveloperRepository;
import com.api.nextspring.repositories.GameRepository;
import com.api.nextspring.repositories.GenreRepository;
import com.api.nextspring.services.GameService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
	private final GameRepository gameRepository;
	private final GenreRepository genreRepository;
	private final DeveloperRepository developerRepository;
	private final ModelMapper modelMapper;

	@Override
	public GameDto create(GameDto gameDto) {
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

		return modelMapper.map(gameRepository.save(gameEntity), GameDto.class);
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

		return gameEntityList.stream().map(game -> modelMapper.map(game, GameDto.class)).toList();
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
}
