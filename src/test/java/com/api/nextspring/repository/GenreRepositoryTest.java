package com.api.nextspring.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.api.nextspring.containers.BaseTest;
import com.api.nextspring.entity.GenreEntity;
import com.api.nextspring.repositories.GenreRepository;
import com.github.javafaker.Faker;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@DataJpaTest
class GenreRepositoryTest extends BaseTest {

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

	@Test
	void shouldSaveAndRetrieveGenre() {
		// given a new genre
		genreRepository.save(genreEntity);

		// when the genre is retrieved
		GenreEntity retrievedGenreEntity = genreRepository.findById(genreEntity.getId()).get();

		// then the saved genre should equal the retrieved genre
		assertThat(retrievedGenreEntity).isEqualTo(genreEntity);
	}

	@Test
	void shouldFindByName() {
		// given a new genre
		genreRepository.save(genreEntity);

		// when the genre is retrieved by name
		List<GenreEntity> retrievedGenreEntityList = genreRepository.searchGenreEntities(genreEntity.getName()).get();

		// then the saved genre should equal the retrieved genre
		assertThat(retrievedGenreEntityList).isNotEmpty();
		assertThat(retrievedGenreEntityList.get(0)).isEqualTo(genreEntity);
	}

	@Test
	void shouldFindAllGenres() {
		// given two new developers
		genreRepository.save(genreEntity);
		genreRepository.save(getGenreEntity());

		// when the developers are retrieved
		List<GenreEntity> genreList = genreRepository.findAll();

		// then the developers should not be empty and should have a size of 2
		assertThat(genreList).isNotEmpty();
		assertThat(genreList.size()).isEqualTo(2);
	}

	@Test
	void shouldUpdateGenre() {
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
	void shouldDeleteGenre() {
		// given a new genre
		genreRepository.save(genreEntity);

		// when the genre is deleted
		genreRepository.deleteById(genreEntity.getId());

		// then the genre should no longer be in the database
		assertThat(genreRepository.findById(genreEntity.getId()).isPresent()).isFalse();
	}
}