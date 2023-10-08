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

/**
 * This interface represents a repository for GenreEntity objects.
 * 
 * @see JpaRepository
 * @see GenreEntity
 * @see RedisConfiguration
 * @see CacheAutoConfiguration
 * @see Cacheable
 * 
 * @implNote This interface represents a repository for GenreEntity objects.
 * 
 * @author Athirson Silva
 */
@ImportAutoConfiguration(classes = { RedisConfiguration.class, CacheAutoConfiguration.class })
public interface GenreRepository extends JpaRepository<GenreEntity, UUID> {

	/**
	 * Checks if a genre with the given name exists in the repository.
	 * 
	 * @param name the name of the genre to check
	 * @return true if a genre with the given name exists, false otherwise
	 */
	@Cacheable(value = "genre", key = "#name")
	boolean existsByName(String name);

	/**
	 * Finds a genre with the given name in the repository.
	 * 
	 * @param name the name of the genre to find
	 * @return an Optional containing the genre with the given name, or an empty
	 *         Optional if no such genre exists
	 */
	@Cacheable(value = "genre", key = "#name")
	Optional<GenreEntity> findByName(String name);

	/**
	 * Searches for genres in the repository that match the given query string.
	 * 
	 * @param query    the query string to search for
	 * @param pageable the Pageable object specifying the page and page size of the
	 *                 results
	 * @return a Page object containing the genres that match the query string
	 */
	@Query("SELECT g FROM GenreEntity g WHERE " +
			"LOWER(g.name) LIKE LOWER(CONCAT('%', :query, '%'))" +
			"OR LOWER(g.description) LIKE LOWER(CONCAT('%', :query, '%'))")
	@Cacheable(value = "genre", key = "#query")
	Page<GenreEntity> searchGenreEntities(String query, Pageable pageable);

	/**
	 * Returns a page of all genres in the repository.
	 * 
	 * @param pageable the Pageable object specifying the page and page size of the
	 *                 results
	 * @return a Page object containing all genres in the repository
	 */
	Page<GenreEntity> findAll(Pageable pageable);
}
