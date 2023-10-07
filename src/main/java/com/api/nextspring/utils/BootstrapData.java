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

	private void createUsers() {
		userRepository.save(UserEntity
				.builder()
				.name("user")
				.email("user@user.com")
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

	private void createAdmin() {
		userRepository.save(UserEntity
				.builder()
				.name("admin")
				.email("admin@admin.com")
				.password(passwordEncoder.encode("password"))
				.roles(Set.of(
						roleRepository.findByName(UserRoles.ADMIN.name()).orElseThrow(
								() -> new RuntimeException("Admin role not found!"))))
				.enabled(true)
				.locked(false)
				.build());

		System.out.println("\n------------ Admin created!!! ------------\n");
	}

	private void createGenres() {
		genreRepository.save(GenreEntity
				.builder()
				.name("No Genre")
				.description("Games without genre")
				.build());

		long genreCount = genreRepository.count();

		if (genreCount == 0L) {
			for (int i = 0; i < 4; i++) {
				genreRepository.save(GenreEntity
						.builder()
						.name(faker.book().genre())
						.description(faker.lorem().sentence())
						.build());
			}
		}
	}

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

	private boolean checkIfAdminAlreadyExists() {
		UserEntity admin = userRepository.findByEmail("admin@admin.com").orElse(null);

		return admin != null;
	}

	private boolean checkIfUserAlreadyExists() {
		UserEntity user = userRepository.findByEmail("user@user.com").orElse(null);

		return user != null;
	}

	private void createUserRole() {
		roleRepository.save(RoleEntity
				.builder()
				.name(UserRoles.USER.name())
				.build());

		System.out.println("\n------------ User role created!!! ------------\n");
	}

	private void createAdminRole() {
		roleRepository.save(RoleEntity
				.builder()
				.name(UserRoles.ADMIN.name())
				.build());

		System.out.println("\n------------ Admin role created!!! ------------\n");
	}

	private boolean checkIfAdminRoleAlreadyExists() {
		RoleEntity admin = roleRepository.findByName(UserRoles.ADMIN.name()).orElse(null);

		return admin != null;
	}

	private boolean checkIfUserRoleAlreadyExists() {
		RoleEntity user = roleRepository.findByName(UserRoles.USER.name()).orElse(null);

		return user != null;
	}

	private boolean checkIfGenreAlreadyExists() {
		GenreEntity genre = genreRepository.findByName("No Genre").orElse(null);

		return genre != null;
	}

	private boolean checkIfDeveloperAlreadyExists() {
		DeveloperEntity developer = developerRepository.findByName("No Developer").orElse(null);

		return developer != null;
	}

	private boolean checkIfGameAlreadyExists() {
		return gameRepository.count() > 0;
	}

	private DeveloperEntity getNoDeveloper() {
		return developerRepository.findByName("No Developer").orElseThrow(
				() -> new RuntimeException("No Developer not found!"));
	}

	private GenreEntity getNoGenre() {
		return genreRepository.findByName("No Genre").orElseThrow(
				() -> new RuntimeException("No Genre not found!"));
	}
}
