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
import com.api.nextspring.entity.DeveloperEntity;
import com.api.nextspring.repositories.DeveloperRepository;
import com.github.javafaker.Faker;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@DataJpaTest
class DeveloperRepositoryTest extends BaseTest {

	@Autowired
	DeveloperRepository developerRepository;

	private DeveloperEntity developerEntity;

	@BeforeEach
	void setUp() {
		developerEntity = getDeveloperEntity();
	}

	private DeveloperEntity getDeveloperEntity() {
		Faker faker = new Faker();

		return DeveloperEntity.builder()
				.name(faker.company().name())
				.description(faker.lorem().paragraph())
				.build();
	}

	@Test
	void shouldSaveAndRetrieveDeveloper() {
		// given a new developer
		developerRepository.save(developerEntity);

		// when the developer is retrieved
		DeveloperEntity retrievedDeveloperEntity = developerRepository.findById(developerEntity.getId()).get();

		// then the saved developer should equal the retrieved developer
		assertThat(retrievedDeveloperEntity).isEqualTo(developerEntity);
	}

	@Test
	void shouldFindByName() {
		// given a new developer
		developerRepository.save(developerEntity);

		// when the developer is retrieved by name
		List<DeveloperEntity> retrievedDeveloperEntityList = developerRepository
				.searchDeveloperEntities(developerEntity.getName()).get();

		// then the saved developer should equal the retrieved developer
		assertThat(retrievedDeveloperEntityList).isNotEmpty();
		assertThat(retrievedDeveloperEntityList.get(0)).isEqualTo(developerEntity);
	}

	@Test
	void shouldFindAllDevelopers() {
		// given two new developers
		developerRepository.save(developerEntity);
		developerRepository.save(getDeveloperEntity());

		// when the developers are retrieved
		List<DeveloperEntity> developers = developerRepository.findAll();

		// then the developers should not be empty and should have a size of 2
		assertThat(developers).isNotEmpty();
		assertThat(developers.size()).isEqualTo(2);
	}

	@Test
	void shouldUpdateDeveloper() {
		// given a saved developer
		developerRepository.save(developerEntity);

		// when the developer is updated
		developerEntity.setDescription("A new description for the developer");
		developerRepository.save(developerEntity);

		// then the updated developer should be persisted in the database
		DeveloperEntity retrievedDeveloperEntity = developerRepository.findById(developerEntity.getId()).get();
		assertThat(retrievedDeveloperEntity.getDescription()).isEqualTo("A new description for the developer");
	}

	@Test
	void shouldDeleteDeveloper() {
		// given a new developer
		developerRepository.save(developerEntity);

		// when the developer is deleted
		developerRepository.deleteById(developerEntity.getId());

		// then the developer should no longer be in the database
		assertThat(developerRepository.findById(developerEntity.getId()).isPresent()).isFalse();
	}
}