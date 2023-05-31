package com.api.nextspring.services;

import java.util.List;
import java.util.UUID;

import com.api.nextspring.payload.GenreDto;
import com.api.nextspring.payload.optionals.OptionalGenreDto;

public interface GenreService {
	public List<GenreDto> findAll();

	public GenreDto findByID(UUID id);

	public GenreDto create(GenreDto request);

	public void deleteByID(UUID id);

	public GenreDto updateByID(UUID id, OptionalGenreDto request);

	public List<GenreDto> searchByKeyword(String query);
}
