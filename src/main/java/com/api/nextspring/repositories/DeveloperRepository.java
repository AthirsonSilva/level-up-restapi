package com.api.nextspring.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.nextspring.config.RedisConfiguration;
import com.api.nextspring.entity.DeveloperEntity;

@ImportAutoConfiguration(classes = { RedisConfiguration.class, CacheAutoConfiguration.class })
public interface DeveloperRepository extends JpaRepository<DeveloperEntity, UUID> {
	@Cacheable(value = "developer", key = "#name")
	Optional<DeveloperEntity> findByName(String name);

	@Cacheable(value = "developer", key = "#name")
	boolean existsByName(String name);

	@Query("SELECT g FROM DeveloperEntity g WHERE " +
			"LOWER(g.name) LIKE LOWER(CONCAT('%', :query, '%'))" +
			"OR LOWER(g.description) LIKE LOWER(CONCAT('%', :query, '%'))")
	@Cacheable(value = "developer", key = "#query")
	Page<DeveloperEntity> searchDeveloperEntities(String query, Pageable pageable);

	Page<DeveloperEntity> findAll(Pageable pageable);
}
