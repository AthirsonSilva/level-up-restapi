package com.api.nextspring;

import com.api.nextspring.entity.RoleEntity;
import com.api.nextspring.entity.UserEntity;
import com.api.nextspring.enums.RolesOptions;
import com.api.nextspring.repositories.RoleRepository;
import com.api.nextspring.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class NextSpringApplication implements CommandLineRunner {
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public NextSpringApplication(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(NextSpringApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) {
		createApplicationRoles();
	}

	private void createApplicationRoles() {
		if (!checkIfAdminRoleAlreadyExists()) createAdminRole();
		if (!checkIfUserRoleAlreadyExists()) createUserRole();
		if (!checkIfAdminUserAlreadyExists()) createAdminUser();

		System.out.println("\n------------ Application roles created! ------------\n");
	}

	private void createAdminUser() {
		userRepository.save(UserEntity
				.builder()
				.name("admin")
				.cpf("00000000000")
				.email("admin@admin.com")
				.password(passwordEncoder.encode("admin"))
				.roles(Set.of(
						roleRepository.findByName(RolesOptions.ADMIN.name()).orElseThrow(
								() -> new RuntimeException("Admin role not found!"))
				))
				.build());
	}

	private boolean checkIfAdminUserAlreadyExists() {
		UserEntity admin = userRepository.findByEmail("admin@admin.com").orElse(null);

		return admin != null;
	}

	private void createUserRole() {
		roleRepository.save(RoleEntity
				.builder()
				.name(RolesOptions.USER.name())
				.build());
	}

	private void createAdminRole() {
		roleRepository.save(RoleEntity
				.builder()
				.name(RolesOptions.ADMIN.name())
				.build());
	}

	private boolean checkIfAdminRoleAlreadyExists() {
		RoleEntity admin = roleRepository.findByName(RolesOptions.ADMIN.name()).orElse(null);

		return admin != null;
	}

	private boolean checkIfUserRoleAlreadyExists() {
		RoleEntity user = roleRepository.findByName(RolesOptions.USER.name()).orElse(null);

		return user != null;
	}
}
