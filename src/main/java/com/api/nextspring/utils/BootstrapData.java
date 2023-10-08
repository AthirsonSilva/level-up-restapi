package com.api.nextspring.utils;

import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.api.nextspring.entity.DeveloperEntity;
import com.api.nextspring.entity.GameEntity;
import com.api.nextspring.entity.GenreEntity;
import com.api.nextspring.entity.RoleEntity;
import com.api.nextspring.entity.UserEntity;
import com.api.nextspring.enums.GameRatingOptions;
import com.api.nextspring.enums.UserRoles;
import com.api.nextspring.repositories.DeveloperRepository;
import com.api.nextspring.repositories.GameRepository;
import com.api.nextspring.repositories.GenreRepository;
import com.api.nextspring.repositories.RoleRepository;
import com.api.nextspring.repositories.UserRepository;
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
public class BootstrapData implements InitializingBean {
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final GenreRepository genreRepository;
	private final DeveloperRepository developerRepository;
	private final GameRepository gameRepository;
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

		System.out.println("\n------------ Application Bootstraped!!! ------------\n");
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
				.password(passwordEncoder.encode("password"))
				.roles(Set.of(
						roleRepository.findByName(UserRoles.USER.name()).orElseThrow(
								() -> new RuntimeException("User role not found!"))))
				.enabled(true)
				.locked(false)
				.build());

		for (int i = 0; i < 4; i++) {
			userRepository.save(UserEntity
					.builder()
					.name(faker.name().fullName())
					.email(faker.internet().emailAddress())
					.photoPath(faker.internet().avatar())
					.password(passwordEncoder.encode("user"))
					.roles(Set.of(
							roleRepository.findByName(UserRoles.USER.name()).orElseThrow(
									() -> new RuntimeException("User role not found!"))))
					.enabled(true)
					.locked(false)
					.build());
		}

		System.out.println("\n------------ User created!!! ------------\n");
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
				.password(passwordEncoder.encode("password"))
				.roles(Set.of(
						roleRepository.findByName(UserRoles.ADMIN.name()).orElseThrow(
								() -> new RuntimeException("Admin role not found!"))))
				.enabled(true)
				.locked(false)
				.build());

		System.out.println("\n------------ Admin created!!! ------------\n");
	}

	/**
	 * creates 4 genres with random data and one genre with name "No Genre"
	 */
	private void createGenres() {
		genreRepository.save(GenreEntity
				.builder()
				.name("No Genre")
				.description("Games without genre")
				.build());

		for (int i = 0; i < 4; i++) {
			genreRepository.save(GenreEntity
					.builder()
					.name(faker.book().genre())
					.description(faker.lorem().sentence())
					.build());
		}
	}

	/**
	 * creates 4 developers with random data and one developer with name "No
	 * Developer"
	 */
	private void createDevelopers() {
		developerRepository.save(DeveloperEntity
				.builder()
				.name("No Developer")
				.description("Game without developer")
				.build());

		for (int i = 0; i < 4; i++) {
			developerRepository.save(DeveloperEntity
					.builder()
					.name(faker.company().name())
					.description(faker.lorem().sentence())
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
					.developer(getNoDeveloper())
					.genre(getNoGenre())
					.year(faker.number().numberBetween(1900, 2023))
					.photoPath("https://avatars.dicebear.com/api/open-peeps/" + faker.name().firstName() + ".svg")
					.build());
		}
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
	 * creates the user role in the database
	 */
	private void createUserRole() {
		roleRepository.save(RoleEntity
				.builder()
				.name(UserRoles.USER.name())
				.build());

		System.out.println("\n------------ User role created!!! ------------\n");
	}

	/**
	 * creates the admin role in the database
	 */
	private void createAdminRole() {
		roleRepository.save(RoleEntity
				.builder()
				.name(UserRoles.ADMIN.name())
				.build());

		System.out.println("\n------------ Admin role created!!! ------------\n");
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
	 * checks if the genre already exists in the database
	 * 
	 * @return whether or not there is at least one genre in the database
	 */
	private boolean checkIfGenreAlreadyExists() {
		GenreEntity genre = genreRepository.findByName("No Genre").orElse(null);

		return genre != null;
	}

	/**
	 * Returns whether or not there is at least one developer in the database
	 * 
	 * @return whether or not there is at least one developer in the database
	 */
	private boolean checkIfDeveloperAlreadyExists() {
		DeveloperEntity developer = developerRepository.findByName("No Developer").orElse(null);

		return developer != null;
	}

	/**
	 * Returns whether or not there is at least one game in the database
	 * 
	 * @return whether or not there is at least one game in the database
	 */
	private boolean checkIfGameAlreadyExists() {
		return gameRepository.count() > 0;
	}

	private boolean checkIfAddressAlreadyExists() {
		return false;
	}

	/**
	 * Returns the developer with name "No Developer" which is the default developer
	 * 
	 * @return the developer with name "No Developer" which is the default developer
	 */
	private DeveloperEntity getNoDeveloper() {
		return developerRepository.findByName("No Developer").orElseThrow(
				() -> new RuntimeException("No Developer not found!"));
	}

	/**
	 * Returns the genre with name "No Genre" which is the default genre
	 * 
	 * @return the genre with name "No Genre" which is the default genre
	 */
	private GenreEntity getNoGenre() {
		return genreRepository.findByName("No Genre").orElseThrow(
				() -> new RuntimeException("No Genre not found!"));
	}
}
