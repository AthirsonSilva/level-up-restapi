package com.api.levelup.utils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.api.levelup.entity.AddressEntity;
import com.api.levelup.entity.DeveloperEntity;
import com.api.levelup.entity.GameEntity;
import com.api.levelup.entity.GenreEntity;
import com.api.levelup.entity.RoleEntity;
import com.api.levelup.entity.UserEntity;
import com.api.levelup.enums.GameRatingOptions;
import com.api.levelup.enums.UserRoles;
import com.api.levelup.repositories.AddressRepository;
import com.api.levelup.repositories.DeveloperRepository;
import com.api.levelup.repositories.GameRepository;
import com.api.levelup.repositories.GenreRepository;
import com.api.levelup.repositories.RoleRepository;
import com.api.levelup.repositories.UserRepository;
import com.github.javafaker.Faker;

import lombok.RequiredArgsConstructor;

/**
 * This class is responsible for creating all the data needed for the
 * application to be used in development.
 * It implements the InitializingBean interface to ensure that the
 * createApplicationRoles() method is called after all the dependencies are
 * injected.
 * The class uses repositories to persist data in the database and the Faker
 * library to generate random data.
 * 
 * @see InitializingBean
 * 
 * @param roleRepository      repository for the role entity
 * @param userRepository      repository for the user entity
 * @param genreRepository     repository for the genre entity
 * @param developerRepository repository for the developer entity
 * @param gameRepository      repository for the game entity
 * @param passwordEncoder     password encoder to encode users's password before
 *                            saving
 * @param faker               library to generate random data
 * 
 * @author Athirson Silva
 */
@Component
@RequiredArgsConstructor
public class DataMigration implements InitializingBean {
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final GenreRepository genreRepository;
	private final DeveloperRepository developerRepository;
	private final GameRepository gameRepository;
	private final AddressRepository addressRepository;
	private final PasswordEncoder passwordEncoder;
	private final Faker faker;

	@Override
	public void afterPropertiesSet() throws Exception {
		createApplicationRoles();
	}

	/**
	 * creates all the data needed for the application to be used in development
	 */
	private void createApplicationRoles() {
		if (!checkIfAdminRoleAlreadyExists())
			createAdminRole();

		if (!checkIfUserRoleAlreadyExists())
			createUserRole();

		if (!checkIfAdminAlreadyExists())
			createAdmin();

		if (!checkIfUserAlreadyExists())
			createUsers();

		if (!checkIfGenreAlreadyExists())
			createGenres();

		if (!checkIfDeveloperAlreadyExists())
			createDevelopers();

		if (!checkIfGameAlreadyExists())
			createGames();
	}

	/**
	 * creates 4 users with random data and one user with name "user"
	 */
	private void createUsers() {
		userRepository.save(UserEntity
				.builder()
				.name("user")
				.email("user@user.com")
				.photoPath(faker.internet().avatar())
				.address(createAddresses())
				.password(passwordEncoder.encode("password"))
				.roles(Set.of(
						roleRepository.findByName(UserRoles.USER.name()).orElseThrow(
								() -> new RuntimeException("User role not found!"))))
				.enabled(true)
				.locked(false)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build());

		for (int i = 0; i < 4; i++) {
			userRepository.save(UserEntity
					.builder()
					.name(faker.name().fullName())
					.email(faker.internet().emailAddress())
					.photoPath(faker.internet().avatar())
					.address(createAddresses())
					.password(passwordEncoder.encode("user"))
					.roles(Set.of(
							roleRepository.findByName(UserRoles.USER.name()).orElseThrow(
									() -> new RuntimeException("User role not found!"))))
					.enabled(true)
					.locked(false)
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.build());
		}
	}

	/**
	 * creates the admin user in the database
	 */
	private void createAdmin() {
		userRepository.save(UserEntity
				.builder()
				.name("admin")
				.email("admin@admin.com")
				.photoPath(faker.internet().avatar())
				.address(createAddresses())
				.password(passwordEncoder.encode("password"))
				.roles(Set.of(
						roleRepository.findByName(UserRoles.ADMIN.name()).orElseThrow(
								() -> new RuntimeException("Admin role not found!"))))
				.enabled(true)
				.locked(false)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build());
	}

	/**
	 * creates 4 genres with random data and one genre with name "No Genre"
	 */
	private void createGenres() {
		for (int i = 0; i < 5; i++) {
			genreRepository.save(GenreEntity
					.builder()
					.name(faker.book().genre())
					.description(faker.lorem().sentence())
					.games(Collections.emptyList())
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.build());
		}
	}

	/**
	 * creates 4 developers with random data and one developer with name "No
	 * Developer"
	 */
	private void createDevelopers() {
		for (int i = 0; i < 5; i++) {
			developerRepository.save(DeveloperEntity
					.builder()
					.name(faker.company().name())
					.description(faker.lorem().sentence())
					.games(Collections.emptyList())
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.build());
		}
	}

	/**
	 * creates 5 games with random data
	 */
	private void createGames() {
		for (int i = 0; i < 5; i++) {
			gameRepository.save(GameEntity
					.builder()
					.name(faker.company().name())
					.grade(GameRatingOptions.values()[faker.number().numberBetween(0, 4)].toString())
					.description(faker.lorem().sentence())
					.developer(getDeveloperByIndex(i))
					.genre(getGenreByIndex(i))
					.year(faker.number().numberBetween(1900, 2023))
					.photoPath(faker.internet().avatar())
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.build());
		}
	}

	/**
	 * creates 5 addresses with random data
	 */
	private AddressEntity createAddresses() {
		return addressRepository.save(AddressEntity
				.builder()
				.street(faker.address().streetName())
				.complement(faker.address().secondaryAddress())
				.neighborhood(faker.address().streetName())
				.city(faker.address().city())
				.state(faker.address().state())
				.zipCode(faker.address().zipCode())
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build());
	}

	/**
	 * creates the user role in the database
	 */
	private void createUserRole() {
		roleRepository.save(RoleEntity
				.builder()
				.name(UserRoles.USER.name())
				.build());
	}

	/**
	 * creates the admin role in the database
	 */
	private void createAdminRole() {
		roleRepository.save(RoleEntity
				.builder()
				.name(UserRoles.ADMIN.name())
				.build());
	}

	/**
	 * creates the user role in the database
	 * 
	 * @return whether or not there is at least one admin in the database
	 */
	private boolean checkIfAdminAlreadyExists() {
		UserEntity admin = userRepository.findByEmail("admin@admin.com").orElse(null);

		return admin != null;
	}

	/**
	 * creates the user role in the database
	 * 
	 * @return whether or not there is at least one user in the database
	 */
	private boolean checkIfUserAlreadyExists() {
		UserEntity user = userRepository.findByEmail("user@user.com").orElse(null);

		return user != null;
	}

	/**
	 * checks if the admin role already exists in the database
	 * 
	 * @return whether or not there is at least one admin role in the database
	 */
	private boolean checkIfAdminRoleAlreadyExists() {
		RoleEntity admin = roleRepository.findByName(UserRoles.ADMIN.name()).orElse(null);

		return admin != null;
	}

	/**
	 * checks if the user role already exists in the database
	 * 
	 * @return whether or not there is at least one user role in the database
	 */
	private boolean checkIfUserRoleAlreadyExists() {
		RoleEntity user = roleRepository.findByName(UserRoles.USER.name()).orElse(null);

		return user != null;
	}

	/**
	 * Returns whether or not there is at least one game in the database
	 * 
	 * @return whether or not there is at least one game in the database
	 */
	private boolean checkIfGameAlreadyExists() {
		return gameRepository.count() > 0;
	}

	/**
	 * Returns whether or not there is at least one developer in the database
	 * 
	 * @return whether or not there is at least one developer in the database
	 */
	private boolean checkIfDeveloperAlreadyExists() {
		return developerRepository.count() > 0;
	}

	/**
	 * Returns whether or not there is at least one genre in the database
	 * 
	 * @return whether or not there is at least one genre in the database
	 */
	private boolean checkIfGenreAlreadyExists() {
		return genreRepository.count() > 0;
	}

	/**
	 * Returns the DeveloperEntity object at the specified index from the list of
	 * all developers.
	 *
	 * @param index the index of the DeveloperEntity object to be returned
	 * @return the DeveloperEntity object at the specified index
	 */
	private DeveloperEntity getDeveloperByIndex(Integer index) {
		return developerRepository.findAll().get(index);
	}

	/**
	 * Returns the genre entity at the specified index in the list of all genres.
	 *
	 * @param index the index of the genre entity to return
	 * @return the genre entity at the specified index
	 */
	private GenreEntity getGenreByIndex(Integer index) {
		return genreRepository.findAll().get(index);
	}
}
