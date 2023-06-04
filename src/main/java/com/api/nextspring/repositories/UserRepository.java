package com.api.nextspring.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.nextspring.config.RedisConfiguration;
import com.api.nextspring.entity.UserEntity;

@ImportAutoConfiguration(classes = { RedisConfiguration.class, CacheAutoConfiguration.class })
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
	@Cacheable(value = "user", key = "#email")
	Optional<UserEntity> findByEmail(String email);

	@Cacheable(value = "user", key = "#cpf")
	boolean existsByEmail(String email);

	@Cacheable(value = "user", key = "#cpf")
	boolean existsByCpf(String cpf);
}
