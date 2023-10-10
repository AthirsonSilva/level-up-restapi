package com.api.nextspring.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.api.nextspring.dto.DeveloperDto;
import com.api.nextspring.dto.GameDto;
import com.api.nextspring.dto.GenreDto;
import com.api.nextspring.dto.export.DeveloperExportDto;
import com.api.nextspring.dto.export.GameExportDto;
import com.api.nextspring.dto.export.GenreExportDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExporterDtoMapper {
	private final ModelMapper modelMapper;

	public <T> DeveloperExportDto toDeveloperExportDto(T object) {
		DeveloperDto formattedDto = modelMapper.map(object, DeveloperDto.class);

		String createdAt = getFormattedDateTime(formattedDto.getCreatedAt());
		String updatedAt = getFormattedDateTime(formattedDto.getUpdatedAt());

		return DeveloperExportDto.builder()
				.id(formattedDto.getId())
				.name(formattedDto.getName())
				.description(formattedDto.getDescription())
				.createdAt(createdAt)
				.updatedAt(updatedAt)
				.build();
	}

	public <T> GameExportDto toGameExportDto(T object) {
		GameDto formattedDto = modelMapper.map(object, GameDto.class);

		String createdAt = getFormattedDateTime(formattedDto.getCreatedAt());
		String updatedAt = getFormattedDateTime(formattedDto.getUpdatedAt());

		return GameExportDto.builder()
				.id(formattedDto.getId())
				.name(formattedDto.getName())
				.description(formattedDto.getDescription())
				.createdAt(createdAt)
				.updatedAt(updatedAt)
				.build();
	}

	public <T> GenreExportDto toGenreExportDto(T object) {
		GenreDto formattedDto = modelMapper.map(object, GenreDto.class);

		String createdAt = getFormattedDateTime(formattedDto.getCreatedAt());
		String updatedAt = getFormattedDateTime(formattedDto.getUpdatedAt());

		return GenreExportDto.builder()
				.id(formattedDto.getId())
				.name(formattedDto.getName())
				.description(formattedDto.getDescription())
				.createdAt(createdAt)
				.updatedAt(updatedAt)
				.build();
	}

	private String getFormattedDateTime(LocalDateTime dateTime) {
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		// Pass the formatted date to the DTO
		return dateFormat.format(dateTime);
	}
}
