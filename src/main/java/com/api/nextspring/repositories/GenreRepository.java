package com.api.nextspring.repositories;

import com.api.nextspring.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, UUID> {
	boolean existsByName(String name);
	Optional<GenreEntity> findByName(String name);
}
