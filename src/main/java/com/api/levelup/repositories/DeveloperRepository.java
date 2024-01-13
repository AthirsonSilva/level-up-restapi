package com.api.levelup.repositories;

import java.util.Optional;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.api.levelup.config.RedisConfiguration;
import com.api.levelup.entity.DeveloperEntity;

/**
 * This interface represents the repository for DeveloperEntity objects.
 * It extends MongoRepository to inherit basic CRUD operations and adds
 * additional
 * methods for caching and searching.
 * 
 * @see MongoRepository
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
public interface DeveloperRepository extends MongoRepository<DeveloperEntity, String> {

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
	@Query("{$or:[{'name': {$regex: ?0, $options: 'i'}}, {'description': {$regex: ?0, $options: 'i'}}]}")
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
