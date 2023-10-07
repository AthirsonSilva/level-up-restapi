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
import com.api.nextspring.repositories.DeveloperRepository;
import com.github.javafaker.Faker;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Testcontainers
class DeveloperRepositoryTest extends PostgresTestContainerConfig {

	@Autowired
	DeveloperRepository developerRepository;

	private DeveloperEntity developerEntity;

	@BeforeEach
	void setUp() {
		developerEntity = getDeveloperEntity();
	}

	@AfterEach
	void tearDown() {
		developerRepository.deleteAll();
	}

	private DeveloperEntity getDeveloperEntity() {
		Faker faker = new Faker();

		return DeveloperEntity.builder()
				.name(faker.company().name())
				.description(faker.lorem().paragraph())
				.build();
	}

	@Test
	public void shouldSaveAndRetrieveDeveloper() {
		// given a new developer
		developerRepository.save(developerEntity);

		// when the developer is retrieved
		DeveloperEntity retrievedDeveloperEntity = developerRepository.findById(developerEntity.getId()).get();

		// then the saved developer should equal the retrieved developer
		assertThat(retrievedDeveloperEntity).isEqualTo(developerEntity);
	}

	@Test
	public void shouldFindByName() {
		// given a new developer
		developerRepository.save(developerEntity);
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.fromString("ASC"), "name"));

		// when the developer is retrieved by name
		List<DeveloperEntity> retrievedDeveloperEntityList = developerRepository
				.searchDeveloperEntities(developerEntity.getName(), pageable).toList();

		// then the saved developer should equal the retrieved developer
		assertThat(retrievedDeveloperEntityList).isNotEmpty();
		assertThat(retrievedDeveloperEntityList.get(0)).isEqualTo(developerEntity);
	}

	@Test
	public void shouldFindAllDevelopers() {
		// given two new developers
		developerRepository.save(developerEntity);
		developerRepository.save(getDeveloperEntity());
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.fromString("ASC"), "name"));

		// when the developers are retrieved
		List<DeveloperEntity> developers = developerRepository.findAll(pageable).toList();

		// then the developers should not be empty and should have a size of 2
		assertThat(developers).isNotEmpty();
		assertThat(developers.size()).isEqualTo(2);
	}

	@Test
	public void shouldUpdateDeveloper() {
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
	public void shouldDeleteDeveloper() {
		// given a new developer
		developerRepository.save(developerEntity);

		// when the developer is deleted
		developerRepository.deleteById(developerEntity.getId());

		// then the developer should no longer be in the database
		assertThat(developerRepository.findById(developerEntity.getId()).isPresent()).isFalse();
	}
}