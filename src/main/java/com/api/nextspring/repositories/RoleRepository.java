package com.api.nextspring.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.nextspring.config.RedisConfiguration;
import com.api.nextspring.entity.RoleEntity;

/**
 * This interface represents the repository for RoleEntity, which extends
 * JpaRepository.
 * It provides methods to interact with the database and cache.
 * 
 * @see JpaRepository
 * @see RoleEntity
 * @see RedisConfiguration
 * @see CacheAutoConfiguration
 * @see Cacheable
 * 
 * @implNote This interface represents the repository for RoleEntity, which
 *           extends JpaRepository.
 *           It provides methods to interact with the database and cache.
 * 
 * @author Athirson Silva
 */
@ImportAutoConfiguration(classes = { RedisConfiguration.class, CacheAutoConfiguration.class })
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
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
