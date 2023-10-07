package com.api.nextspring.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.api.nextspring.dto.DeveloperDto;
import com.api.nextspring.entity.DeveloperEntity;
import com.api.nextspring.repositories.DeveloperRepository;
import com.github.javafaker.Faker;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class DeveloperServiceTest {

	@InjectMocks
	private DeveloperService developerService;

	@Mock
	private DeveloperRepository developerRepository;

	private DeveloperEntity developerEntity;

	private DeveloperDto developerDto;

	@BeforeEach
	void setUp() {
		developerEntity = getDeveloperEntity();
		developerDto = getDeveloperDto();
	}

	private DeveloperEntity getDeveloperEntity() {
		Faker faker = new Faker();

		return DeveloperEntity.builder()
				.name(faker.company().name())
				.description(faker.lorem().paragraph())
				.build();
	}

	private DeveloperDto getDeveloperDto() {
		Faker faker = new Faker();

		return DeveloperDto.builder()
				.name(faker.company().name())
				.description(faker.lorem().paragraph())
				.build();
	}

	@Test
	public void shouldSaveAndRetrieveDeveloper() {
		// given a new developer
		DeveloperDto savedDeveloperEntity = developerService.create(developerDto);

		Mockito.when(developerRepository.findById(savedDeveloperEntity.getId())).thenReturn(Optional.of(developerEntity));
		Mockito.when(developerRepository.save(developerEntity)).thenReturn(developerEntity);

		// when the developer is retrieved
		DeveloperDto retrievedDeveloperEntity = developerService.findByID(savedDeveloperEntity.getId());

		// then the saved developer should equal the retrieved developer
		assertThat(retrievedDeveloperEntity).isEqualTo(savedDeveloperEntity);

		Mockito.verify(developerRepository).save(any(DeveloperEntity.class));
	}
}
