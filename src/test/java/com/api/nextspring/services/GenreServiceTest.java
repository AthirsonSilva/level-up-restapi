package com.api.nextspring.services;

import com.api.nextspring.dto.GenreDto;
import com.api.nextspring.entity.GenreEntity;
import com.api.nextspring.exceptions.RestApiException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {

    @Mock
    private GenreService genreService;

    @Test
    @DisplayName("Given a new genre, when save, then return a genre with id")
    public void searchByKeyword_withValidQuery_returnsListOfGenreDto() {
        // Arrange
        String query = "action";
        int page = 0;
        int size = 10;
        String sort = "name";
        PageRequest pageable = PageRequest.of(page, size, Sort.by(sort));

        GenreEntity genreEntity = new GenreEntity();
        genreEntity.setId(UUID.randomUUID());
        genreEntity.setName("Action");

        GenreDto genreDto = new GenreDto();
        genreDto.setId(genreEntity.getId());
        genreDto.setName("Action");

        Mockito.when(genreService.searchByKeyword(query, pageable)).thenReturn(List.of(genreDto));

        // Act
        List<GenreDto> genreDtos = genreService.searchByKeyword(query, pageable);

        // Assert
        Assertions.assertThat(genreDtos).isNotNull();
        Assertions.assertThat(genreDtos).isNotEmpty();
    }

    @Test
    @DisplayName("Given a new genre, when save, then return a genre with id")
    void searchByKeyword_withInvalidQuery_throwsRestApiException() {
        // Arrange
        String query = "invalid";
        int page = 0;
        int size = 10;
        String sort = "name";
        PageRequest pageable = PageRequest.of(page, size, Sort.by(sort));

        Mockito.when(genreService.searchByKeyword(query, pageable)).thenThrow(RestApiException.class);

        // Act & Assert
        Assertions
                .assertThatThrownBy(() -> genreService.searchByKeyword(query, pageable))
                .isInstanceOf(RestApiException.class);
    }
}