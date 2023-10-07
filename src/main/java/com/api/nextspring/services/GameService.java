package com.api.nextspring.services;

import java.util.List;
import java.util.UUID;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import com.api.nextspring.dto.GameDto;
import com.api.nextspring.dto.optionals.OptionalGameDto;

import jakarta.servlet.http.HttpServletResponse;

public interface GameService {
	public GameDto create(GameDto gameDto);

	public List<GameDto> searchByKeyword(String query);

	public List<GameDto> findAll();

	public GameDto updateById(UUID id, OptionalGameDto gameDto);

	public void deleteById(UUID id);

	public GameDto findByID(UUID id);

	public GameDto uploadPhoto(UUID id, MultipartFile file);

	public InputStreamResource downloadPhotoByGame(UUID id);

	public void exportToExcel(HttpServletResponse response);
}
