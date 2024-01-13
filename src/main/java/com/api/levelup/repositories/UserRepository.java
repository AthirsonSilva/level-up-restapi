package com.api.levelup.repositories;

import java.util.Optional;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.api.levelup.config.RedisConfiguration;
import com.api.levelup.entity.UserEntity;

/**
 * This interface represents a repository for managing user entities.
 * It extends the MongoRepository interface and provides additional methods for
 * caching and modifying user data.
 * 
 * @see MongoRepository
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
public interface UserRepository extends MongoRepository<UserEntity, String> {

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
	 * Checks if a user entity exists in the database by email and password.
	 * 
	 * @param email    the email of the user to check
	 * @param password the password of the user to check
	 * @return true if the user entity exists, false otherwise
	 */
	boolean existsByEmailAndPassword(String email, String password);
}
