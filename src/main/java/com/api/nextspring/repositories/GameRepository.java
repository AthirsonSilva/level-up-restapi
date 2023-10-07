package com.api.nextspring.repositories;

import java.util.UUID;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.nextspring.config.RedisConfiguration;
import com.api.nextspring.entity.GameEntity;

@ImportAutoConfiguration(classes = { RedisConfiguration.class, CacheAutoConfiguration.class })
public interface GameRepository extends JpaRepository<GameEntity, UUID> {
	@Cacheable(value = "game", key = "#name")
	boolean existsByName(String name);

	@Query("SELECT g FROM GameEntity g WHERE " +
			"LOWER(g.name) LIKE LOWER(CONCAT('%', :query, '%'))" +
			"OR LOWER(g.description) LIKE LOWER(CONCAT('%', :query, '%'))" +
			"OR LOWER(g.grade) LIKE LOWER(CONCAT('%', :query, '%'))" +
			"OR LOWER(g.genre.name) LIKE LOWER(CONCAT('%', :query, '%'))" +
			"OR LOWER(g.genre.description) LIKE LOWER(CONCAT('%', :query, '%'))")
	@Cacheable(value = "game", key = "#query")
	Page<GameEntity> searchGameEntities(String query, Pageable pageable);

	Page<GameEntity> findAll(Pageable pageable);
}
