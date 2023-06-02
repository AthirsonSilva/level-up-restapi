package com.api.nextspring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.api.nextspring.utils.BootstrapData;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class NextSpringApplication implements CommandLineRunner {
	private final BootstrapData bootstrapData;

	public static void main(String[] args) {
		SpringApplication.run(NextSpringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		bootstrapData.run();
	}
}
