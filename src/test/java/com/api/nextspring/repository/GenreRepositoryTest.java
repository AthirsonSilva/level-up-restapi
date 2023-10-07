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
import com.api.nextspring.entity.GenreEntity;
import com.api.nextspring.repositories.GenreRepository;
import com.github.javafaker.Faker;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Testcontainers
class GenreRepositoryTest extends PostgresTestContainerConfig {

	@Autowired
	GenreRepository genreRepository;

	private GenreEntity genreEntity;

	@BeforeEach
	void setUp() {
		genreEntity = getGenreEntity();
	}

	private GenreEntity getGenreEntity() {
		Faker faker = new Faker();

		return GenreEntity.builder()
				.name(faker.lorem().word())
				.description(faker.lorem().paragraph())
				.build();
	}

	@AfterEach
	void tearDown() {
		genreRepository.deleteAll();
	}

	@Test
	public void shouldSaveAndRetrieveGenre() {
		// given a new genre
		genreRepository.save(genreEntity);

		// when the genre is retrieved
		GenreEntity retrievedGenreEntity = genreRepository.findById(genreEntity.getId()).get();

		// then the saved genre should equal the retrieved genre
		assertThat(retrievedGenreEntity).isEqualTo(genreEntity);
	}

	@Test
	public void shouldFindByName() {
		// given a new genre
		genreRepository.save(genreEntity);
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.fromString("ASC"), "name"));

		// when the genre is retrieved by name
		List<GenreEntity> retrievedGenreEntityList = genreRepository.searchGenreEntities(genreEntity.getName(), pageable)
				.toList();

		// then the saved genre should equal the retrieved genre
		assertThat(retrievedGenreEntityList).isNotEmpty();
		assertThat(retrievedGenreEntityList.get(0)).isEqualTo(genreEntity);
	}

	@Test
	public void shouldFindAllGenres() {
		// given two new developers
		genreRepository.save(genreEntity);
		genreRepository.save(getGenreEntity());
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.fromString("ASC"), "name"));

		// when the developers are retrieved
		List<GenreEntity> genreList = genreRepository.findAll(pageable).toList();

		// then the developers should not be empty and should have a size of 2
		assertThat(genreList).isNotEmpty();
		assertThat(genreList.size()).isEqualTo(2);
	}

	@Test
	public void shouldUpdateGenre() {
		// given a saved genre
		genreRepository.save(genreEntity);

		// when the genre is updated
		genreEntity.setDescription("A new description for the genre");
		genreRepository.save(genreEntity);

		// then the updated genre should be persisted in the database
		GenreEntity retrievedGenreEntity = genreRepository.findById(genreEntity.getId()).get();
		assertThat(retrievedGenreEntity.getDescription()).isEqualTo("A new description for the genre");
	}

	@Test
	public void shouldDeleteGenre() {
		// given a new genre
		genreRepository.save(genreEntity);

		// when the genre is deleted
		genreRepository.deleteById(genreEntity.getId());

		// then the genre should no longer be in the database
		assertThat(genreRepository.findById(genreEntity.getId()).isPresent()).isFalse();
	}
}