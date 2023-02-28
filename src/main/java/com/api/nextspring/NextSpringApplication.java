package com.api.nextspring;

import com.api.nextspring.entity.RoleEntity;
import com.api.nextspring.repositories.RoleRepository;
import com.api.nextspring.enums.RolesOptions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class NextSpringApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;
	public NextSpringApplication(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public ModelMapper getModelMapper() {
		return new ModelMapper();
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
		if (!checkIfRoleAlreadyExists()) createApplicationRoles();
	}

	private void createApplicationRoles() {
		RoleEntity admin = RoleEntity
			.builder()
			.name(RolesOptions.ADMIN)
			.build();

		RoleEntity user = RoleEntity
			.builder()
			.name(RolesOptions.USER)
			.build();

		roleRepository.saveAll(List.of(admin, user));
	}

	private boolean checkIfRoleAlreadyExists() {
		RoleEntity admin = roleRepository.findByName(RolesOptions.ADMIN).orElse(null);
		RoleEntity user = roleRepository.findByName(RolesOptions.USER).orElse(null);

		return admin != null && user != null;
	}
}
