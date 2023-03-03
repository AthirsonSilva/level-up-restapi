package com.api.nextspring.repositories;

import com.api.nextspring.entity.DeveloperEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeveloperRepository extends JpaRepository<DeveloperEntity, UUID> {
	Optional<DeveloperEntity> findByName(String name);

	boolean existsByName(String name);
}
