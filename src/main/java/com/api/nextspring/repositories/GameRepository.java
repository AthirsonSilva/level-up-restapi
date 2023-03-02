package com.api.nextspring.repositories;

import com.api.nextspring.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, UUID> {
	boolean existsByName(String name);
	@Query(
			"SELECT g FROM GameEntity g WHERE " +
					"LOWER(g.name) LIKE LOWER(CONCAT('%', :query, '%'))" +
					"OR LOWER(g.description) LIKE LOWER(CONCAT('%', :query, '%'))" +
					"OR LOWER(g.grade) LIKE LOWER(CONCAT('%', :query, '%'))" +
					"OR LOWER(g.genre.name) LIKE LOWER(CONCAT('%', :query, '%'))" +
					"OR LOWER(g.genre.description) LIKE LOWER(CONCAT('%', :query, '%'))"
	)
	Optional<List<GameEntity>> searchGameEntities(String query);
}
