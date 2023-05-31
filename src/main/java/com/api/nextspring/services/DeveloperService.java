package com.api.nextspring.services;

import java.util.List;
import java.util.UUID;

import com.api.nextspring.payload.DeveloperDto;
import com.api.nextspring.payload.optionals.OptionalDeveloperDto;

public interface DeveloperService {
	public DeveloperDto create(DeveloperDto developerDto);

	public List<DeveloperDto> findAll();

	public DeveloperDto findByID(UUID id);

	public DeveloperDto updateByID(UUID id, OptionalDeveloperDto developerDto);

	public void deleteByID(UUID id);
}
