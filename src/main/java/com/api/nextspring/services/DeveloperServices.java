package com.api.nextspring.services;

import com.api.nextspring.entity.DeveloperEntity;
import com.api.nextspring.exceptions.RestApiException;
import com.api.nextspring.payload.DeveloperDto;
import com.api.nextspring.payload.optionals.OptionalDeveloperDto;
import com.api.nextspring.repositories.DeveloperRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeveloperServices {
	private final DeveloperRepository developerRepository;
	private final ModelMapper modelMapper;

	public DeveloperDto create(DeveloperDto request) {
		if (developerRepository.existsByName(request.getName())) {
			throw new RestApiException(HttpStatus.NOT_FOUND, "Developer with given name already exists");
		}

		DeveloperEntity developerEntity = DeveloperEntity
				.builder()
				.name(request.getName())
				.description(request.getDescription())
				.build();

		return modelMapper.map(developerRepository.save(developerEntity), DeveloperDto.class);
	}

	public List<DeveloperDto> getAll() {
		List<DeveloperEntity> developerEntities = developerRepository.findAll();

		return developerEntities.stream().map(
				developerEntity -> modelMapper.map(developerEntity, DeveloperDto.class)
		).collect(Collectors.toList());
	}

	public DeveloperDto getDeveloperByUUID(UUID id) {
		DeveloperEntity developerEntity = developerRepository.findById(id)
				.orElseThrow(() -> new RestApiException(HttpStatus.NOT_FOUND, "Developer with given name not found"));

		return modelMapper.map(developerEntity, DeveloperDto.class);
	}

	public DeveloperDto updateDeveloper(UUID id, OptionalDeveloperDto request) {
		DeveloperEntity developerEntity = developerRepository.findById(id)
				.orElseThrow(() -> new RestApiException(HttpStatus.NOT_FOUND, "Developer with given id not found"));

		if (request.getName() != null)
			developerEntity.setName(request.getName());

		if (request.getDescription() != null)
			developerEntity.setDescription(request.getDescription());

		return modelMapper.map(developerRepository.save(developerEntity), DeveloperDto.class);
	}

	public void delete(UUID id) {
		DeveloperEntity developerEntity = developerRepository.findById(id)
				.orElseThrow(() -> new RestApiException(HttpStatus.NOT_FOUND, "Developer with given name not found"));

		developerRepository.delete(developerEntity);
	}
}
