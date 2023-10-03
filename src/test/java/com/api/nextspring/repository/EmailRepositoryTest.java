package com.api.nextspring.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.api.nextspring.containers.BaseTest;
import com.api.nextspring.entity.EmailEntity;
import com.api.nextspring.repositories.EmailRepository;
import com.github.javafaker.Faker;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@DataJpaTest
public class EmailRepositoryTest extends BaseTest {

	@Autowired
	private EmailRepository emailRepository;

	private EmailEntity emailEntity;

	@BeforeEach
	void setUp() {
		emailEntity = generateEmailEntity();
	}

	private EmailEntity generateEmailEntity() {
		Faker faker = new Faker();

		return EmailEntity.builder()
				.content(faker.lorem().paragraph())
				.subject(faker.lorem().sentence())
				.destination(faker.internet().emailAddress())
				.username(faker.name().username())
				.sender(faker.internet().emailAddress())
				.build();
	}

	@AfterEach
	void tearDown() {
		emailRepository.deleteAll();
	}

	@Test
	void shouldSaveAndRetrieveEmail() {
		// given a new email
		emailRepository.save(emailEntity);

		// when the email is saved and retrieved
		EmailEntity foundEmail = emailRepository.findById(emailEntity.getId()).get();

		// then the saved email should equal the retrieved email
		assertThat(emailEntity).isEqualTo(foundEmail);
		assertThat(emailEntity.getId()).isNotNull();
	}

	@Test
	void shouldSaveAndFindAllEmails() {
		// given two new emails
		emailRepository.save(emailEntity);
		emailRepository.save(generateEmailEntity());

		// when the emails are retrieved
		List<EmailEntity> emails = emailRepository.findAll();

		// then the emails should not be empty and should have a size of 2
		assertThat(emails).isNotEmpty();
		assertThat(emails.size()).isEqualTo(2);
	}
}
