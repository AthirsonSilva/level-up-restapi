package com.api.nextspring;

import com.api.nextspring.repositories.GameRepository;
import com.api.nextspring.repositories.GenreRepository;
import com.api.nextspring.repositories.RoleRepository;
import com.api.nextspring.repositories.UserRepository;
import com.api.nextspring.utils.BootstrapData;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class NextSpringApplication implements CommandLineRunner {
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final GenreRepository genreRepository;

	public static void main(String[] args) {
		SpringApplication.run(NextSpringApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		BootstrapData bootstrapData = new BootstrapData(
				roleRepository, userRepository, genreRepository, passwordEncoder
		);

		bootstrapData.run();
	}
}
