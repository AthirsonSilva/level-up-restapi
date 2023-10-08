package com.api.nextspring.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.api.nextspring.config.RedisConfiguration;
import com.api.nextspring.entity.UserEntity;

/**
 * This interface represents a repository for managing user entities.
 * It extends the JpaRepository interface and provides additional methods for
 * caching and modifying user data.
 * 
 * @see JpaRepository
 * @see UserEntity
 * @see RedisConfiguration
 * @see CacheAutoConfiguration
 * @see Cacheable
 * 
 * @implNote This interface represents a repository for managing user entities.
 * 
 * @author
 */
@ImportAutoConfiguration(classes = { RedisConfiguration.class, CacheAutoConfiguration.class })
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

	/**
	 * Retrieves a user entity from the cache by email.
	 * 
	 * @param email the email of the user to retrieve
	 * @return an Optional containing the user entity, or empty if not found
	 */
	@Cacheable(value = "user", key = "#email")
	Optional<UserEntity> findByEmail(String email);

	/**
	 * Checks if a user entity exists in the cache by email.
	 * 
	 * @param email the email of the user to check
	 * @return true if the user entity exists, false otherwise
	 */
	@Cacheable(value = "user", key = "#email")
	Boolean existsByEmail(String email);

	/**
	 * Enables a user by ID.
	 * 
	 * @param id the ID of the user to enable
	 * @return the number of rows affected by the update
	 */
	@Modifying
	@Query(value = "update users set enabled = true, locked = false where id = ?1", nativeQuery = true)
	Integer enableUser(UUID id);

	/**
	 * Checks if a user entity exists in the database by email and password.
	 * 
	 * @param email    the email of the user to check
	 * @param password the password of the user to check
	 * @return true if the user entity exists, false otherwise
	 */
	boolean existsByEmailAndPassword(String email, String password);
}
