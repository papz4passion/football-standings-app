package com.sapient.football.service;

import com.sapient.football.cache.CustomCacheManager;
import com.sapient.football.dto.CountryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FootballService.
 * Demonstrates TDD approach and high test coverage.
 */
@ExtendWith(MockitoExtension.class)
class FootballServiceTest {
    
    @Mock
    private FootballApiService apiService;
    
    @Mock
    private CustomCacheManager<String, List<?>> cacheManager;
    
    @InjectMocks
    private FootballService footballService;
    
    @BeforeEach
    void setUp() {
        // Set TTL values using reflection
        ReflectionTestUtils.setField(footballService, "countriesTtl", 86400000L);
        ReflectionTestUtils.setField(footballService, "leaguesTtl", 3600000L);
        ReflectionTestUtils.setField(footballService, "teamsTtl", 3600000L);
        ReflectionTestUtils.setField(footballService, "standingsTtl", 300000L);
    }
    
    @Test
    void testGetCountries_FromCache() {
        // Given
        List<CountryDTO> cachedCountries = Arrays.asList(
                new CountryDTO("1", "England", "logo1.png"),
                new CountryDTO("2", "Spain", "logo2.png")
        );
        when(cacheManager.get("countries")).thenReturn(Optional.of(cachedCountries));
        
        // When
        List<CountryDTO> result = footballService.getCountries();
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("England", result.get(0).getCountryName());
        verify(cacheManager).get("countries");
        verify(apiService, never()).getCountries();
    }
    
    @Test
    void testGetCountries_FromApi() {
        // Given
        List<CountryDTO> apiCountries = Arrays.asList(
                new CountryDTO("1", "England", "logo1.png"),
                new CountryDTO("2", "Spain", "logo2.png")
        );
        when(cacheManager.get("countries")).thenReturn(Optional.empty());
        when(apiService.getCountries()).thenReturn(apiCountries);
        
        // When
        List<CountryDTO> result = footballService.getCountries();
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(cacheManager).get("countries");
        verify(apiService).getCountries();
        verify(cacheManager).put(eq("countries"), anyList(), anyLong());
    }
    
    @Test
    void testGetCountries_OfflineMode_NoCache() {
        // Given
        footballService.setOfflineMode(true);
        when(cacheManager.get("countries")).thenReturn(Optional.empty());
        
        // When
        List<CountryDTO> result = footballService.getCountries();
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(apiService, never()).getCountries();
    }
    
    @Test
    void testSetOfflineMode() {
        // When
        footballService.setOfflineMode(true);
        
        // Then
        assertTrue(footballService.isOfflineMode());
        
        // When
        footballService.setOfflineMode(false);
        
        // Then
        assertFalse(footballService.isOfflineMode());
    }
    
    @Test
    void testClearCache() {
        // When
        footballService.clearCache();
        
        // Then
        verify(cacheManager).clear();
    }
}
