package com.api.nextspring.repositories;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.api.nextspring.config.RedisConfiguration;
import com.api.nextspring.entity.EmailEntity;

/**
 * This interface represents the repository for EmailEntity objects. It extends
 * the MongoRepository interface
 * and inherits its methods for basic CRUD operations.
 * 
 * @see MongoRepository
 * @see EmailEntity
 * 
 * @implNote This interface represents the repository for EmailEntity objects.
 *           It extends the MongoRepository interface
 * 
 * @author Athirson Silva
 */
@ImportAutoConfiguration(classes = { RedisConfiguration.class, CacheAutoConfiguration.class })
public interface EmailRepository extends MongoRepository<EmailEntity, String> {

}
