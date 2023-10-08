package com.api.nextspring.config;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

/**
 * Configuration class for Redis cache.
 * 
 * @see RedisCacheConfiguration
 * @param cacheConfiguration()                 Returns a RedisCacheConfiguration
 *                                             object with default cache
 *                                             configuration settings.
 * 
 * @param redisCacheManagerBuilderCustomizer() Returns a
 *                                             RedisCacheManagerBuilderCustomizer
 *                                             object that customizes the
 *                                             RedisCacheManager builder.
 * 
 * @author Athirson Silva
 */
@Configuration
public class RedisConfiguration {

	/**
	 * Returns a RedisCacheConfiguration object with default cache configuration
	 * settings.
	 * The entry time-to-live (TTL) is set to 10 minutes, caching of null values is
	 * disabled,
	 * and values are serialized with the GenericJackson2JsonRedisSerializer.
	 *
	 * @return RedisCacheConfiguration object with default cache configuration
	 *         settings
	 */
	@Bean
	RedisCacheConfiguration cacheConfiguration() {
		return RedisCacheConfiguration
				.defaultCacheConfig()
				.entryTtl(java.time.Duration.ofMinutes(10))
				.disableCachingNullValues()
				.serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
	}

	/**
	 * Returns a RedisCacheManagerBuilderCustomizer object that customizes the
	 * RedisCacheManager builder.
	 * Two cache configurations are added to the builder: "itemCache" with a TTL of
	 * 10 minutes,
	 * and "customerCache" with a TTL of 5 minutes.
	 *
	 * @return RedisCacheManagerBuilderCustomizer object that customizes the
	 *         RedisCacheManager builder
	 */
	@Bean
	RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
		return (builder) -> builder
				.withCacheConfiguration("itemCache",
						RedisCacheConfiguration.defaultCacheConfig().entryTtl(java.time.Duration.ofMinutes(10)))
				.withCacheConfiguration("customerCache",
						RedisCacheConfiguration.defaultCacheConfig().entryTtl(java.time.Duration.ofMinutes(5)));
	}
}
