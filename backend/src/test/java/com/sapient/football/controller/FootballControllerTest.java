package com.sapient.football.controller;

import com.sapient.football.dto.CountryDTO;
import com.sapient.football.service.FootballService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for FootballController.
 */
@WebMvcTest(FootballController.class)
class FootballControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private FootballService footballService;
    
    @Test
    void testGetCountries_Success() throws Exception {
        // Given
        List<CountryDTO> countries = Arrays.asList(
                new CountryDTO("1", "England", "logo1.png"),
                new CountryDTO("2", "Spain", "logo2.png")
        );
        when(footballService.getCountries()).thenReturn(countries);
        
        // When & Then
        mockMvc.perform(get("/api/countries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].country_id").value("1"))
                .andExpect(jsonPath("$[0].country_name").value("England"))
                .andExpect(jsonPath("$[1].country_id").value("2"))
                .andExpect(jsonPath("$[1].country_name").value("Spain"));
    }
    
    @Test
    void testGetLeagues_MissingParameter() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/leagues")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testGetTeams_MissingParameter() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testGetStandings_MissingParameter() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/standings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testToggleOfflineMode() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/offline-mode")
                        .param("enabled", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Offline mode enabled"));
    }
    
    @Test
    void testClearCache() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/cache")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Cache cleared successfully"));
    }
}
