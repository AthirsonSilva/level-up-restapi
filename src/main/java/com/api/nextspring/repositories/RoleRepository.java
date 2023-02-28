package com.api.nextspring.repositories;

import com.api.nextspring.entity.RoleEntity;
import com.api.nextspring.enums.RolesOptions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
	Optional<RoleEntity> findByName(RolesOptions name);
}
