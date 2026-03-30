package com.sportcalendar.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportcalendar.team.TeamController;
import com.sportcalendar.team.domain.TeamService;
import com.sportcalendar.team.dto.TeamDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeamController.class)
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeamService teamService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should find teams by name search")
    void shouldSearchTeams() throws Exception {
        TeamDto team = TeamDto.builder().name("Real Madrid").build();
        when(teamService.searchTeamsByName("Real")).thenReturn(List.of(team));

        mockMvc.perform(get("/api/teams/search").param("name", "Real"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Real Madrid"));
    }

    @Test
    @DisplayName("Should update team and return 200 status")
    void shouldUpdateTeam() throws Exception {
        TeamDto updateDto = TeamDto.builder().name("Updated FC").build();
        when(teamService.updateTeam(eq(1), any(TeamDto.class))).thenReturn(updateDto);

        mockMvc.perform(put("/api/teams/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated FC"));
    }
}
