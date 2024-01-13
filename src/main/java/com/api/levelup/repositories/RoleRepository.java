package com.api.levelup.repositories;

import java.util.Optional;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.api.levelup.config.RedisConfiguration;
import com.api.levelup.entity.RoleEntity;

/**
 * This interface represents the repository for RoleEntity, which extends
 * MongoRepository.
 * It provides methods to interact with the database and cache.
 * 
 * @see MongoRepository
 * @see RoleEntity
 * @see RedisConfiguration
 * @see CacheAutoConfiguration
 * @see Cacheable
 * 
 * @implNote This interface represents the repository for RoleEntity, which
 *           extends MongoRepository.
 *           It provides methods to interact with the database and cache.
 * 
 * @author Athirson Silva
 */
@ImportAutoConfiguration(classes = { RedisConfiguration.class, CacheAutoConfiguration.class })
public interface RoleRepository extends MongoRepository<RoleEntity, String> {
	/**
	 * This method finds a RoleEntity by its name from the cache or database.
	 * 
	 * @param name the name of the role to find
	 * @return an Optional containing the RoleEntity if found, otherwise an empty
	 *         Optional
	 */
	@Cacheable(value = "role", key = "#name")
	Optional<RoleEntity> findByName(String name);
}
