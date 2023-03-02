package com.api.nextspring.services;

import com.api.nextspring.entity.GameEntity;
import com.api.nextspring.entity.GenreEntity;
import com.api.nextspring.exceptions.RestApiException;
import com.api.nextspring.payload.GameDto;
import com.api.nextspring.repositories.GameRepository;
import com.api.nextspring.repositories.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServices {
	private final GameRepository gameRepository;
	private final GenreRepository genreRepository;
	private final ModelMapper modelMapper;

	public GameDto createGame(GameDto gameDto) {
		if (gameDto.getGenreId() == null || !genreRepository.existsById(gameDto.getGenreId())) {
			gameDto.setGenreId(getNoGenreEntity().getId());
		}

		GenreEntity genreEntity = genreRepository.findById(gameDto.getGenreId()).orElse(getNoGenreEntity());

		genreRepository.save(genreEntity);

		GameEntity gameEntity = GameEntity
				.builder()
				.description(gameDto.getDescription())
				.year(gameDto.getYear())
				.grade(gameDto.getGrade().name())
				.genre(genreEntity)
				.build();

		if (gameRepository.existsByName(gameDto.getName()))
			throw new RestApiException(HttpStatus.BAD_REQUEST, "Game with given name already exists!");
		else
			gameEntity.setName(gameDto.getName());

		return modelMapper.map(gameRepository.save(gameEntity), GameDto.class);
	}

	public List<GameDto> getGame(String query) {
		List<GameEntity> gameEntityList = gameRepository.searchGameEntities(query).orElseThrow(
				() -> new RestApiException(HttpStatus.NOT_FOUND, "Game with given information was not found!")
		);

		return gameEntityList.stream().map(game -> modelMapper.map(game, GameDto.class)).toList();
	}

	private GenreEntity getNoGenreEntity() {
		return genreRepository.findByName("No Genre").orElse(
				GenreEntity
						.builder()
						.name("No Genre")
						.description("No Genre")
						.build()
		);
	}
}
