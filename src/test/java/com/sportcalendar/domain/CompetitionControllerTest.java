package com.sportcalendar.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportcalendar.competition.CompetitionController;
import com.sportcalendar.competition.domain.StageService;
import com.sportcalendar.competition.dto.StageDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompetitionController.class)
class CompetitionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StageService stageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return list of all stages")
    void shouldReturnAllStages() throws Exception {
        StageDto stage = StageDto.builder().name("League Stage").build();
        when(stageService.getAllStages()).thenReturn(List.of(stage));

        mockMvc.perform(get("/api/competitions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("League Stage"));
    }

    @Test
    @DisplayName("Should create new stage and return 201 status")
    void shouldCreateStage() throws Exception {
        StageDto requestDto = StageDto.builder().name("Knockout").build();
        StageDto savedDto = StageDto.builder().stageId(1).name("Knockout").build();

        when(stageService.createStage(any(StageDto.class))).thenReturn(savedDto);

        mockMvc.perform(post("/api/competitions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.stageId").value(1))
                .andExpect(jsonPath("$.name").value("Knockout"));
    }

    @Test
    @DisplayName("Should delete stage and return 204 status")
    void shouldDeleteStage() throws Exception {
        mockMvc.perform(delete("/api/competitions/1"))
                .andExpect(status().isNoContent());
    }
}