package com.api.nextspring.services;

import java.util.List;
import java.util.UUID;

import com.api.nextspring.dto.GenreDto;
import com.api.nextspring.dto.optionals.OptionalGenreDto;

import jakarta.servlet.http.HttpServletResponse;

public interface GenreService {
	public List<GenreDto> findAll(Integer page, Integer size, String sort, String direction);

	public GenreDto findByID(UUID id);

	public GenreDto create(GenreDto request);

	public void deleteByID(UUID id);

	public GenreDto updateByID(UUID id, OptionalGenreDto request);

	public List<GenreDto> searchByKeyword(String query, Integer page, Integer size, String sort, String direction);

	public void exportToExcel(HttpServletResponse response);
}
