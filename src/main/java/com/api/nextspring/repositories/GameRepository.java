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

/**
 * This interface represents the repository for GameEntity objects. It extends
 * the JpaRepository interface
 * and provides additional methods for searching and caching game entities.
 * 
 * @see JpaRepository
 * @see GameEntity
 * @see RedisConfiguration
 * @see CacheAutoConfiguration
 * @see Cacheable
 * 
 * @implNote This interface represents the repository for GameEntity objects. It
 *           extends the JpaRepository interface
 * 
 * @author Athirson Silva
 */
@ImportAutoConfiguration(classes = { RedisConfiguration.class, CacheAutoConfiguration.class })
public interface GameRepository extends JpaRepository<GameEntity, UUID> {
	/**
	 * Checks if a game with the given name exists in the cache.
	 *
	 * @param name the name of the game to check
	 * @return true if a game with the given name exists in the cache, false
	 *         otherwise
	 */
	@Cacheable(value = "game", key = "#name")
	boolean existsByName(String name);

	/**
	 * Searches for game entities that match the given query string. The search is
	 * performed on the name, description,
	 * grade, genre name, and genre description fields of the game entities. The
	 * search is case-insensitive and partial
	 * matches are allowed.
	 *
	 * @param query    the query string to search for
	 * @param pageable the pagination information
	 * @return a page of game entities that match the given query string
	 */
	@Query("SELECT g FROM GameEntity g WHERE " +
			"LOWER(g.name) LIKE LOWER(CONCAT('%', :query, '%'))" +
			"OR LOWER(g.description) LIKE LOWER(CONCAT('%', :query, '%'))" +
			"OR LOWER(g.grade) LIKE LOWER(CONCAT('%', :query, '%'))" +
			"OR LOWER(g.genre.name) LIKE LOWER(CONCAT('%', :query, '%'))" +
			"OR LOWER(g.genre.description) LIKE LOWER(CONCAT('%', :query, '%'))")
	@Cacheable(value = "game", key = "#query")
	Page<GameEntity> searchGameEntities(String query, Pageable pageable);

	/**
	 * Returns a page of all games in the repository.
	 *
	 * @param pageable the pagination information
	 * @return a page of game entities
	 */
	Page<GameEntity> findAll(Pageable pageable);
}
