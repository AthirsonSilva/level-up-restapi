package com.api.nextspring.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.nextspring.config.RedisConfiguration;
import com.api.nextspring.entity.DeveloperEntity;

@ImportAutoConfiguration(classes = { RedisConfiguration.class, CacheAutoConfiguration.class })
public interface DeveloperRepository extends JpaRepository<DeveloperEntity, UUID> {
	@Cacheable(value = "developer", key = "#name")
	Optional<DeveloperEntity> findByName(String name);

	@Cacheable(value = "developer", key = "#name")
	boolean existsByName(String name);
}
