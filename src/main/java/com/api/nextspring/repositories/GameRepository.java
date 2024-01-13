package com.api.nextspring.repositories;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.api.nextspring.config.RedisConfiguration;
import com.api.nextspring.entity.GameEntity;

/**
 * This interface represents the repository for GameEntity objects. It extends
 * the MongoRepository interface
 * and provides additional methods for searching and caching game entities.
 * 
 * @see MongoRepository
 * @see GameEntity
 * @see RedisConfiguration
 * @see CacheAutoConfiguration
 * @see Cacheable
 * 
 * @implNote This interface represents the repository for GameEntity objects. It
 *           extends the MongoRepository interface
 * 
 * @author Athirson Silva
 */
@ImportAutoConfiguration(classes = { RedisConfiguration.class, CacheAutoConfiguration.class })
public interface GameRepository extends MongoRepository<GameEntity, String> {
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
	@Query("{$or: [{name: {$regex: ?0, $options: 'i'}}, {description: {$regex: ?0, $options: 'i'}}, {grade: {$regex: ?0, $options: 'i'}}, {genre.name: {$regex: ?0, $options: 'i'}}, {genre.description: {$regex: ?0, $options: 'i'}}]}")
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
