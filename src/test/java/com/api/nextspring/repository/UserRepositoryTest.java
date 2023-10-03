package com.api.nextspring.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.api.nextspring.containers.BaseTest;
import com.api.nextspring.entity.RoleEntity;
import com.api.nextspring.entity.UserEntity;
import com.api.nextspring.repositories.RoleRepository;
import com.api.nextspring.repositories.UserRepository;
import com.github.javafaker.Faker;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@DataJpaTest
class UserRepositoryTest extends BaseTest {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	private UserEntity userEntity;
	private RoleEntity roleEntity;

	@BeforeEach
	void setUp() {
		roleEntity = RoleEntity.builder().name("ROLE_USER").build();
		roleRepository.save(roleEntity);

		Set<RoleEntity> roles = new HashSet<>();
		roles.add(roleEntity);

		userEntity = getUserEntity(roles);
	}

	private UserEntity getUserEntity(Set<RoleEntity> roles) {
		Faker faker = new Faker();

		return UserEntity.builder()
				.name(faker.name().fullName())
				.email(faker.internet().emailAddress())
				.password(faker.internet().password())
				.enabled(true)
				.locked(false)
				.roles(roles)
				.build();
	}

	@Test
	void shouldSaveAndRetrieveUser() {
		// given a new user
		userRepository.save(userEntity);

		// when the user is retrieved
		UserEntity retrievedUserEntity = userRepository.findById(userEntity.getId()).get();

		// then the saved user should equal the retrieved user
		assertThat(retrievedUserEntity).isEqualTo(userEntity);
	}

	@Test
	void shouldFindByEmail() {
		// given a new user
		userRepository.save(userEntity);

		// when the user is retrieved by email
		UserEntity retrievedUserEntity = userRepository.findByEmail(userEntity.getEmail()).get();

		// then the saved user should equal the retrieved user
		assertThat(retrievedUserEntity).isEqualTo(userEntity);
	}

	@Test
	void shouldFindAllUsers() {
		// given two new users
		userRepository.save(userEntity);
		userRepository.save(getUserEntity(new HashSet<>()));

		// when the users are retrieved
		List<UserEntity> users = userRepository.findAll();

		// then the users should not be empty and should have a size of 2
		assertThat(users).isNotEmpty();
		assertThat(users.size()).isEqualTo(2);
	}

	@Test
	void shouldUpdateUser() {
		// given a saved user
		userRepository.save(userEntity);

		// when the user is updated
		userEntity.setName("Jane Doe");
		userRepository.save(userEntity);

		// then the updated user should be persisted in the database
		UserEntity retrievedUserEntity = userRepository.findById(userEntity.getId()).get();

		assertThat(retrievedUserEntity.getName()).isEqualTo("Jane Doe");
	}

	@Test
	void shouldDeleteUser() {
		// Save the user
		userRepository.save(userEntity);

		// Delete the user
		userRepository.deleteById(userEntity.getId());

		// Assert that the user is no longer in the database
		assertThat(userRepository.findById(userEntity.getId()).isPresent()).isFalse();
	}
}