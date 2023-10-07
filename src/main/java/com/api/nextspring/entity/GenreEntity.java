package com.api.nextspring.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "genres")
public class GenreEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false)
	private String description;

	@OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GameEntity> games;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
}
