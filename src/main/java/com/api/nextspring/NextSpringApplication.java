package com.api.nextspring;

import com.api.nextspring.entity.RoleEntity;
import com.api.nextspring.enums.RolesOptions;
import com.api.nextspring.repositories.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NextSpringApplication implements CommandLineRunner {
	private final RoleRepository roleRepository;

	public NextSpringApplication(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
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

		System.out.println("\n------------ Application roles created! ------------\n");
	}

	private void createUserRole() {
		roleRepository.save(RoleEntity
				.builder()
				.name(RolesOptions.USER)
				.build());
	}

	private void createAdminRole() {
		roleRepository.save(RoleEntity
				.builder()
				.name(RolesOptions.ADMIN)
				.build());
	}

	private boolean checkIfAdminRoleAlreadyExists() {
		RoleEntity admin = roleRepository.findByName(RolesOptions.ADMIN).orElse(null);

		return admin != null;
	}

	private boolean checkIfUserRoleAlreadyExists() {
		RoleEntity user = roleRepository.findByName(RolesOptions.USER).orElse(null);

		return user != null;
	}
}
