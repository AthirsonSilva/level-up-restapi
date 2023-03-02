package com.api.nextspring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "games")
public class GameEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private int year;

	@Column(nullable = false)
	private String grade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "genre_id", nullable = false)
	private GenreEntity genre;
}
