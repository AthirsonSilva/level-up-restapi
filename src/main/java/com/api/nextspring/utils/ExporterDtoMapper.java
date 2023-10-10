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

/**
 * This class is responsible for mapping objects to their corresponding DTOs for
 * export.
 * 
 * @see DeveloperExportDto
 * @see GameExportDto
 * @see GenreExportDto
 * @see ModelMapper
 * 
 * @author Athirson Silva
 */
@Service
@RequiredArgsConstructor
public class ExporterDtoMapper {
	private final ModelMapper modelMapper;

	/**
	 * Maps a Developer object to a DeveloperExportDto object.
	 * 
	 * @param object The Developer object to be mapped.
	 * @return The corresponding DeveloperExportDto object.
	 */
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

	/**
	 * Maps a Game object to a GameExportDto object.
	 * 
	 * @param object The Game object to be mapped.
	 * @return The corresponding GameExportDto object.
	 */
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

	/**
	 * Maps a Genre object to a GenreExportDto object.
	 * 
	 * @param object The Genre object to be mapped.
	 * @return The corresponding GenreExportDto object.
	 */
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

	/**
	 * Formats a LocalDateTime object to a string in the format "dd/MM/yyyy".
	 * 
	 * @param dateTime The LocalDateTime object to be formatted.
	 * @return The formatted date string.
	 */
	private String getFormattedDateTime(LocalDateTime dateTime) {
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		// Pass the formatted date to the DTO
		return dateFormat.format(dateTime);
	}
}
