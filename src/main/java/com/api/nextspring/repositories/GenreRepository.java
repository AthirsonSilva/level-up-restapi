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
import com.api.nextspring.entity.GenreEntity;

@ImportAutoConfiguration(classes = { RedisConfiguration.class, CacheAutoConfiguration.class })
public interface GenreRepository extends JpaRepository<GenreEntity, UUID> {
	@Cacheable(value = "genre", key = "#name")
	boolean existsByName(String name);

	@Cacheable(value = "genre", key = "#name")
	Optional<GenreEntity> findByName(String name);

	@Query("SELECT g FROM GenreEntity g WHERE " +
			"LOWER(g.name) LIKE LOWER(CONCAT('%', :query, '%'))" +
			"OR LOWER(g.description) LIKE LOWER(CONCAT('%', :query, '%'))")
	@Cacheable(value = "genre", key = "#query")
	Page<GenreEntity> searchGenreEntities(String query, Pageable pageable);

	Page<GenreEntity> findAll(Pageable pageable);
}
