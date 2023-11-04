package com.api.nextspring.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.api.nextspring.dto.GenreDto;
import com.api.nextspring.entity.GenreEntity;
import com.api.nextspring.exceptions.RestApiException;
import com.api.nextspring.repositories.GenreRepository;
import com.api.nextspring.services.impl.GenreServiceImpl;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {

	@Mock
	private GenreRepository genreRepository;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private GenreServiceImpl genreService;

	public GenreServiceImplTest() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void searchByKeyword_withValidQuery_returnsListOfGenreDto() {
		// Arrange
		String query = "action";
		int page = 0;
		int size = 10;
		String sort = "name";
		PageRequest pageable = PageRequest.of(page, size, Sort.by(sort));

		GenreEntity genreEntity = new GenreEntity();
		genreEntity.setId(UUID.randomUUID());
		genreEntity.setName("Action");

		when(genreRepository.searchGenreEntities(query, pageable))
				.thenReturn(new PageImpl<>(Collections.singletonList(genreEntity)));

		GenreDto genreDto = new GenreDto();
		genreDto.setId(genreEntity.getId());
		genreDto.setName("Action");

		when(modelMapper.map(genreEntity, GenreDto.class)).thenReturn(genreDto);

		// Act
		List<GenreDto> genreDtos = genreService.searchByKeyword(query, pageable);

		// Assert
		assertEquals(1, genreDtos.size());
		assertEquals(genreDto, genreDtos.get(0));
	}

	@Test
	void searchByKeyword_withInvalidQuery_throwsRestApiException() {
		// Arrange
		String query = "invalid";
		int page = 0;
		int size = 10;
		String sort = "name";
		PageRequest pageable = PageRequest.of(page, size, Sort.by(sort));

		when(genreRepository.searchGenreEntities(query, pageable)).thenReturn(new PageImpl<>(Collections.emptyList()));

		// Act & Assert
		assertThrows(RestApiException.class, () -> genreService.searchByKeyword(query, pageable));
	}
}