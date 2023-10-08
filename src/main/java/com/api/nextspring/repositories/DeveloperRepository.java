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

/**
 * This interface represents the repository for DeveloperEntity objects.
 * It extends JpaRepository to inherit basic CRUD operations and adds additional
 * methods for caching and searching.
 * 
 * @see JpaRepository
 * @see DeveloperEntity
 * @see RedisConfiguration
 * @see CacheAutoConfiguration
 * @see Cacheable
 * 
 * @implNote This interface represents the repository for DeveloperEntity
 *           objects.
 * 
 * @author Athirson Silva
 */
@ImportAutoConfiguration(classes = { RedisConfiguration.class, CacheAutoConfiguration.class })
public interface DeveloperRepository extends JpaRepository<DeveloperEntity, UUID> {

	/**
	 * Retrieves a DeveloperEntity object from the cache by name.
	 * 
	 * @param name the name of the developer to retrieve
	 * @return an Optional containing the DeveloperEntity object if found, otherwise
	 *         an empty Optional
	 */
	@Cacheable(value = "developer", key = "#name")
	Optional<DeveloperEntity> findByName(String name);

	/**
	 * Checks if a DeveloperEntity object with the given name exists in the cache.
	 * 
	 * @param name the name of the developer to check
	 * @return true if a DeveloperEntity object with the given name exists,
	 *         otherwise false
	 */
	@Cacheable(value = "developer", key = "#name")
	boolean existsByName(String name);

	/**
	 * Searches for DeveloperEntity objects in the cache that match the given query
	 * string.
	 * The search is case-insensitive and matches against both the name and
	 * description fields.
	 * 
	 * @param query    the query string to search for
	 * @param pageable the pagination information for the search results
	 * @return a Page containing the search results
	 */
	@Query("SELECT g FROM DeveloperEntity g WHERE " +
			"LOWER(g.name) LIKE LOWER(CONCAT('%', :query, '%'))" +
			"OR LOWER(g.description) LIKE LOWER(CONCAT('%', :query, '%'))")
	@Cacheable(value = "developer", key = "#query")
	Page<DeveloperEntity> searchDeveloperEntities(String query, Pageable pageable);

	/**
	 * Retrieves all DeveloperEntity objects from the cache.
	 * 
	 * @param pageable the pagination information for the results
	 * @return a Page containing all DeveloperEntity objects
	 */
	Page<DeveloperEntity> findAll(Pageable pageable);
}
