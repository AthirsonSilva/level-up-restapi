package com.api.nextspring.services;

import com.api.nextspring.dto.DeveloperDto;
import com.api.nextspring.util.ObjectCreationUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeveloperServiceTest {

    @Mock
    private DeveloperService developerService;

    @Test
    @DisplayName("Given a new developer, when save, then return a developer with id")
    public void testCreateDeveloper_whenSaveDeveloper_thenReturnDeveloperWithId() {
        // given a new developer
        DeveloperDto developerDto = ObjectCreationUtils.getDeveloperDto();

        Mockito.when(developerService.findByID(developerDto.getId())).thenReturn(developerDto);

        // when the developer is retrieved
        DeveloperDto retrievedDeveloperEntity = developerService.findByID(developerDto.getId());

        // then the saved developer should equal the retrieved developer
        Assertions.assertThat(retrievedDeveloperEntity).isEqualTo(developerDto);
    }
}
