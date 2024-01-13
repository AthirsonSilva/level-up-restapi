package com.api.nextspring.repositories;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.api.nextspring.config.RedisConfiguration;
import com.api.nextspring.entity.AddressEntity;
import com.api.nextspring.entity.DeveloperEntity;

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
public interface AddressRepository extends MongoRepository<AddressEntity, String> {
}
