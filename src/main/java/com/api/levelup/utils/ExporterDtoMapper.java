package com.api.levelup.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.api.levelup.dto.DeveloperDto;
import com.api.levelup.dto.GameDto;
import com.api.levelup.dto.GenreDto;
import com.api.levelup.dto.UserDto;
import com.api.levelup.dto.export.DeveloperExportDto;
import com.api.levelup.dto.export.GameExportDto;
import com.api.levelup.dto.export.GenreExportDto;
import com.api.levelup.dto.export.UserExportDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

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
@Log4j2
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

		log.info("Formatted DTO: {}", formattedDto);

		DeveloperExportDto exportDto = DeveloperExportDto.builder()
				.id(formattedDto.getId())
				.name(formattedDto.getName())
				.description(formattedDto.getDescription())
				.createdAt(createdAt)
				.updatedAt(updatedAt)
				.build();

		log.info("Formatted DTO: {}", exportDto);

		return exportDto;
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

		log.info("Formatted DTO: {}", formattedDto);

		GameExportDto exportDto = GameExportDto.builder()
				.id(formattedDto.getId())
				.name(formattedDto.getName())
				.description(formattedDto.getDescription())
				.createdAt(createdAt)
				.updatedAt(updatedAt)
				.build();

		log.info("Formatted DTO: {}", exportDto);

		return exportDto;
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

		log.info("Formatted DTO: {}", formattedDto);

		GenreExportDto exportDto = GenreExportDto.builder()
				.id(formattedDto.getId())
				.name(formattedDto.getName())
				.description(formattedDto.getDescription())
				.createdAt(createdAt)
				.updatedAt(updatedAt)
				.build();

		log.info("Formatted DTO: {}", exportDto);

		return exportDto;
	}

	/**
	 * Maps a User object to a UserExportDto object.
	 * 
	 * @param object The User object to be mapped.
	 * @return The corresponding UserExportDto object.
	 */
	public <T> UserExportDto toUserExportDto(T object) {
		UserDto formattedDto = modelMapper.map(object, UserDto.class);

		String createdAt = getFormattedDateTime(formattedDto.getCreatedAt());
		String updatedAt = getFormattedDateTime(formattedDto.getUpdatedAt());

		log.info("Formatted DTO: {}", formattedDto);

		UserExportDto exportDto = UserExportDto.builder()
				.id(formattedDto.getId())
				.name(formattedDto.getName())
				.email(formattedDto.getEmail())
				.photoPath(formattedDto.getPhotoPath())
				.zipCode(formattedDto.getAddress().getZipCode())
				.street(formattedDto.getAddress().getStreet())
				.complement(formattedDto.getAddress().getComplement())
				.neighborhood(formattedDto.getAddress().getNeighborhood())
				.city(formattedDto.getAddress().getCity())
				.state(formattedDto.getAddress().getState())
				.createdAt(createdAt)
				.updatedAt(updatedAt)
				.build();

		log.info("Formatted DTO: {}", exportDto);

		return exportDto;
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
