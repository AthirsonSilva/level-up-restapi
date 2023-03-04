package com.api.nextspring.repositories;

import com.api.nextspring.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, UUID> {
	boolean existsByName(String name);

	Optional<GenreEntity> findByName(String name);

	@Query(
			"SELECT g FROM GenreEntity g WHERE " +
					"LOWER(g.name) LIKE LOWER(CONCAT('%', :query, '%'))" +
					"OR LOWER(g.description) LIKE LOWER(CONCAT('%', :query, '%'))"
	)
	Optional<List<GenreEntity>> searchGenreEntities(String query);
}
