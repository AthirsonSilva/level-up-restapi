package com.api.nextspring.services;

import java.util.List;
import java.util.UUID;

import com.api.nextspring.payload.GameDto;
import com.api.nextspring.payload.optionals.OptionalGameDto;

public interface GameService {
	public GameDto create(GameDto gameDto);

	public List<GameDto> searchByKeyword(String query);

	public List<GameDto> findAll();

	public GameDto updateById(UUID id, OptionalGameDto gameDto);

	public void deleteById(UUID id);

	public GameDto findByID(UUID id);
}
