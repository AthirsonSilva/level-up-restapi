package com.api.nextspring.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.api.nextspring.containers.PostgresTestContainerConfig;
import com.api.nextspring.entity.DeveloperEntity;
import com.api.nextspring.entity.GameEntity;
import com.api.nextspring.entity.GenreEntity;
import com.api.nextspring.enums.GameRatingOptions;
import com.api.nextspring.repositories.DeveloperRepository;
import com.api.nextspring.repositories.GameRepository;
import com.api.nextspring.repositories.GenreRepository;
import com.github.javafaker.Faker;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Testcontainers
class GameRepositoryTest extends PostgresTestContainerConfig {

	@Autowired
	GameRepository gameRepository;

	@Autowired
	GenreRepository genreRepository;

	@Autowired
	DeveloperRepository developerRepository;

	private GameEntity gameEntity;
	private GenreEntity genreEntity;
	private DeveloperEntity developerEntity;

	@BeforeEach
	void setUp() {
		genreEntity = GenreEntity.builder()
				.name("Action")
				.description(
						"Action games usually involve elements of physical conflict, such as fast paced fighting, combat, platform games, and so on.")
				.build();
		genreRepository.save(genreEntity);

		developerEntity = DeveloperEntity
				.builder()
				.name("Rockstar Games")
				.description(
						"Rockstar Games, Inc. is an American video game publisher based in New York City. The company was established in December 1998 as a subsidiary of Take-Two Interactive, using the assets Take-Two had previously acquired from BMG Interactive.")
				.build();
		developerRepository.save(developerEntity);

		gameEntity = getGameEntity();
	}

	@AfterEach
	void tearDown() {
		gameRepository.deleteAll();
		genreRepository.deleteAll();
		developerRepository.deleteAll();
	}

	private GameEntity getGameEntity() {
		Faker faker = new Faker();

		return GameEntity.builder()
				.name(faker.gameOfThrones().character())
				.description(faker.lorem().paragraph())
				.year(faker.number().numberBetween(1990, 2020))
				.grade(GameRatingOptions.values()[faker.number().numberBetween(0, 4)].toString())
				.genre(genreEntity)
				.developer(developerEntity)
				.build();
	}

	@Test
	public void shouldSaveAndRetrieveGame() {
		// given a new game
		gameRepository.save(gameEntity);

		// when the game is retrieved
		GameEntity retrievedGameEntity = gameRepository.findById(gameEntity.getId()).get();

		// then the saved game should equal the retrieved game
		assertThat(retrievedGameEntity).isEqualTo(gameEntity);
	}

	@Test
	public void shouldFindByName() {
		// given a new game
		gameRepository.save(gameEntity);
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.fromString("ASC"), "name"));

		// when the game is retrieved by name
		List<GameEntity> retrievedGameEntityList = gameRepository.searchGameEntities(gameEntity.getName(), pageable)
				.toList();

		// then the saved game should equal the retrieved game
		assertThat(retrievedGameEntityList).isNotEmpty();
		assertThat(retrievedGameEntityList.get(0)).isEqualTo(gameEntity);
	}

	@Test
	public void shouldFindAllGames() {
		// given two new games
		gameRepository.save(gameEntity);
		gameRepository.save(getGameEntity());
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.fromString("ASC"), "name"));

		// when the games are retrieved
		List<GameEntity> games = gameRepository.findAll(pageable).toList();

		// then the games should not be empty and should have a size of 2
		assertThat(games).isNotEmpty();
		assertThat(games.size()).isEqualTo(2);
	}

	@Test
	public void shouldUpdateGame() {
		// given a saved game
		gameRepository.save(gameEntity);

		// when the game is updated
		gameEntity.setDescription("A new description for the game");
		gameRepository.save(gameEntity);

		// then the updated game should be persisted in the database
		GameEntity retrievedGameEntity = gameRepository.findById(gameEntity.getId()).get();
		assertThat(retrievedGameEntity.getDescription()).isEqualTo("A new description for the game");
	}

	@Test
	public void shouldDeleteGame() {
		// given a new game
		gameRepository.save(gameEntity);

		// when the game is deleted
		gameRepository.deleteById(gameEntity.getId());

		// then the game should no longer be in the database
		assertThat(gameRepository.findById(gameEntity.getId()).isPresent()).isFalse();
	}

	// Add more test cases here to cover other scenarios, such as testing custom
	// queries or relationships.
}