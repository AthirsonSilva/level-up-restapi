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

@ImportAutoConfiguration(classes = { RedisConfiguration.class, CacheAutoConfiguration.class })
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
	@Cacheable(value = "user", key = "#email")
	Optional<UserEntity> findByEmail(String email);

	@Cacheable(value = "user", key = "#email")
	Boolean existsByEmail(String email);

	@Modifying
	@Query(value = "update users set enabled = true, locked = false where id = ?1", nativeQuery = true)
	Integer enableUser(UUID id);

	boolean existsByEmailAndPassword(String email, String password);
}
