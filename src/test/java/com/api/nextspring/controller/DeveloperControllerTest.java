package com.api.nextspring.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.api.nextspring.controllers.DeveloperController;
import com.api.nextspring.dto.DeveloperDto;
import com.api.nextspring.dto.Response;
import com.api.nextspring.repositories.DeveloperRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class DeveloperControllerTest {

	@Mock
	private MockMvc mockMvc;

	@Mock
	private DeveloperRepository developerRepository;

	@Mock
	private DeveloperController developerController;

	@Mock
	private ObjectMapper objectMapper;

	@Test
	public void shouldSaveAndRetrieveDeveloper() throws Exception {
		// given a new developer
		DeveloperDto developer = getDeveloperDto();

		// when the developer is retrieved
		ResponseEntity<Response<String, DeveloperDto>> response = developerController.create(developer, null);

		// then - assertion
		assertThat(response.getBody().getData()).isEqualTo(developer);
	}

	private DeveloperDto getDeveloperDto() {
		Faker faker = new Faker();

		return DeveloperDto.builder()
				.name(faker.company().name())
				.description(faker.lorem().paragraph())
				.build();
	}
}
