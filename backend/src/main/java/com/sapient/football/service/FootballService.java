package com.sapient.football.service;

import com.sapient.football.cache.CustomCacheManager;
import com.sapient.football.dto.CountryDTO;
import com.sapient.football.dto.LeagueDTO;
import com.sapient.football.dto.StandingDTO;
import com.sapient.football.dto.TeamDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Main service layer for football data operations.
 * Implements caching strategy and offline mode support.
 * Follows Single Responsibility and Dependency Inversion principles.
 */
@Slf4j
@Service
public class FootballService {
    
    private final FootballApiService apiService;
    private final CustomCacheManager<String, List<?>> cacheManager;
    
    @Value("${cache.ttl.countries:86400000}")
    private long countriesTtl;
    
    @Value("${cache.ttl.leagues:3600000}")
    private long leaguesTtl;
    
    @Value("${cache.ttl.teams:3600000}")
    private long teamsTtl;
    
    @Value("${cache.ttl.standings:300000}")
    private long standingsTtl;
    
    private boolean offlineMode = false;
    
    public FootballService(FootballApiService apiService, CustomCacheManager<String, List<?>> cacheManager) {
        this.apiService = apiService;
        this.cacheManager = cacheManager;
    }
    
    /**
     * Gets all countries with caching support.
     * 
     * @return List of countries
     */
    @SuppressWarnings("unchecked")
    public List<CountryDTO> getCountries() {
        String cacheKey = "countries";
        
        // Try cache first
        var cachedData = cacheManager.get(cacheKey);
        if (cachedData.isPresent()) {
            log.info("Returning countries from cache");
            return (List<CountryDTO>) cachedData.get();
        }
        
        // If offline mode and no cache, return empty
        if (offlineMode) {
            log.warn("Offline mode: No cached data available for countries");
            return List.of();
        }
        
        // Fetch from API and cache
        List<CountryDTO> countries = apiService.getCountries();
        if (!countries.isEmpty()) {
            cacheManager.put(cacheKey, countries, countriesTtl);
        }
        
        return countries;
    }
    
    /**
     * Gets leagues for a specific country with caching support.
     * 
     * @param countryId country ID
     * @return List of leagues
     */
    @SuppressWarnings("unchecked")
    public List<LeagueDTO> getLeagues(String countryId) {
        String cacheKey = "leagues_" + countryId;
        
        // Try cache first
        var cachedData = cacheManager.get(cacheKey);
        if (cachedData.isPresent()) {
            log.info("Returning leagues from cache for country: {}", countryId);
            return (List<LeagueDTO>) cachedData.get();
        }
        
        // If offline mode and no cache, return empty
        if (offlineMode) {
            log.warn("Offline mode: No cached data available for leagues (country: {})", countryId);
            return List.of();
        }
        
        // Fetch from API and cache
        List<LeagueDTO> leagues = apiService.getLeagues(countryId);
        if (!leagues.isEmpty()) {
            cacheManager.put(cacheKey, leagues, leaguesTtl);
        }
        
        return leagues;
    }
    
    /**
     * Gets teams for a specific league with caching support.
     * 
     * @param leagueId league ID
     * @return List of teams
     */
    @SuppressWarnings("unchecked")
    public List<TeamDTO> getTeams(String leagueId) {
        String cacheKey = "teams_" + leagueId;
        
        // Try cache first
        var cachedData = cacheManager.get(cacheKey);
        if (cachedData.isPresent()) {
            log.info("Returning teams from cache for league: {}", leagueId);
            return (List<TeamDTO>) cachedData.get();
        }
        
        // If offline mode and no cache, return empty
        if (offlineMode) {
            log.warn("Offline mode: No cached data available for teams (league: {})", leagueId);
            return List.of();
        }
        
        // Fetch from API and cache
        List<TeamDTO> teams = apiService.getTeams(leagueId);
        if (!teams.isEmpty()) {
            cacheManager.put(cacheKey, teams, teamsTtl);
        }
        
        return teams;
    }
    
    /**
     * Gets standings for a specific league with optional team filter.
     * 
     * @param leagueId league ID
     * @param teamName optional team name filter
     * @return List of standings
     */
    @SuppressWarnings("unchecked")
    public List<StandingDTO> getStandings(String leagueId, String teamName) {
        String cacheKey = "standings_" + leagueId;
        
        // Try cache first
        var cachedData = cacheManager.get(cacheKey);
        List<StandingDTO> standings;
        
        if (cachedData.isPresent()) {
            log.info("Returning standings from cache for league: {}", leagueId);
            standings = (List<StandingDTO>) cachedData.get();
        } else {
            // If offline mode and no cache, return empty
            if (offlineMode) {
                log.warn("Offline mode: No cached data available for standings (league: {})", leagueId);
                return List.of();
            }
            
            // Fetch from API and cache
            standings = apiService.getStandings(leagueId);
            if (!standings.isEmpty()) {
                cacheManager.put(cacheKey, standings, standingsTtl);
            }
        }
        
        // Filter by team name if provided
        if (teamName != null && !teamName.isBlank()) {
            standings = standings.stream()
                    .filter(s -> s.getTeamName() != null && 
                                s.getTeamName().toLowerCase().contains(teamName.toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        return standings;
    }
    
    /**
     * Enables or disables offline mode.
     * 
     * @param enabled true to enable offline mode
     */
    public void setOfflineMode(boolean enabled) {
        this.offlineMode = enabled;
        log.info("Offline mode {}", enabled ? "enabled" : "disabled");
    }
    
    /**
     * Checks if offline mode is enabled.
     * 
     * @return true if offline mode is enabled
     */
    public boolean isOfflineMode() {
        return offlineMode;
    }
    
    /**
     * Clears all cached data.
     */
    public void clearCache() {
        cacheManager.clear();
        log.info("All cache cleared");
    }
}
