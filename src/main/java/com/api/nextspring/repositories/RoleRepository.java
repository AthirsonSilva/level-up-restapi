package com.api.nextspring.repositories;

import com.api.nextspring.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
	Optional<RoleEntity> findByName(String name);
}
