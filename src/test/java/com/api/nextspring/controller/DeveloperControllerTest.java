package com.api.nextspring.controller;

import com.api.nextspring.controllers.DeveloperController;
import com.api.nextspring.dto.DeveloperDto;
import com.api.nextspring.repositories.DeveloperRepository;
import com.api.nextspring.services.DeveloperService;
import com.api.nextspring.services.LinkingService;
import com.api.nextspring.util.ObjectCreationUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeveloperController.class)
@ActiveProfiles("dev")
public class DeveloperControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeveloperRepository developerRepository;

    @MockBean
    private DeveloperService developerService;

    @MockBean
    private LinkingService linkingService;

    @Test
    @DisplayName("Given a new developer, when save, then return a developer with id")
    public void testCreateDeveloper_whenSaveDeveloper_thenReturnDeveloperWithId() throws Exception {
        // given a new developer
        DeveloperDto developerDto = ObjectCreationUtils.getDeveloperDto();
        developerDto.setId(UUID.randomUUID());

        Mockito.when(developerService.findByID(developerDto.getId())).thenReturn(developerDto);

        // when the developer is retrieved
        mockMvc.perform(get("/api/v1/developers")
                        .param("id", developerDto.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andReturn();
    }

}
