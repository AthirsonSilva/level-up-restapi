package com.api.nextspring.repositories;

import com.api.nextspring.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
	Optional<UserEntity> findByEmail(String email);
	boolean existsByEmail(String email);
	boolean existsByCpf(String cpf);
}
