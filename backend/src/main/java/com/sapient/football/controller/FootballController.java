package com.sapient.football.controller;

import com.sapient.football.config.MetricsConfig;
import com.sapient.football.dto.CountryDTO;
import com.sapient.football.dto.LeagueDTO;
import com.sapient.football.dto.StandingDTO;
import com.sapient.football.dto.TeamDTO;
import com.sapient.football.service.FootballService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Football data endpoints.
 * Implements RESTful API design and HATEOAS principles.
 */
@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "Football API", description = "API endpoints for fetching football data")
public class FootballController {
    
    private final FootballService footballService;
    private final MetricsConfig.FootballMetrics metrics;
    
    public FootballController(FootballService footballService, MetricsConfig.FootballMetrics metrics) {
        this.footballService = footballService;
        this.metrics = metrics;
    }
    
    /**
     * GET /api/countries - Fetch all available countries.
     * 
     * @return List of countries
     */
    @Operation(summary = "Get all countries", description = "Fetches all available countries with their details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved countries",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CountryDTO.class))),
        @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    @GetMapping("/countries")
    public ResponseEntity<List<CountryDTO>> getCountries() {
        log.info("GET /api/countries - Fetching all countries");
        metrics.getCountriesRequestCounter().increment();
        List<CountryDTO> countries = footballService.getCountries();
        return ResponseEntity.ok(countries);
    }
    
    /**
     * GET /api/leagues?country_id={id} - Fetch leagues by country.
     * 
     * @param countryId country ID
     * @return List of leagues
     */
    @Operation(summary = "Get leagues by country", description = "Fetches all leagues for a specific country")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved leagues",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = LeagueDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid country ID"),
        @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    @GetMapping("/leagues")
    public ResponseEntity<List<LeagueDTO>> getLeagues(
            @Parameter(description = "Country ID to fetch leagues for", required = true)
            @RequestParam("country_id") String countryId) {
        
        log.info("GET /api/leagues?country_id={} - Fetching leagues", countryId);
        metrics.getLeaguesRequestCounter().increment();
        
        if (countryId == null || countryId.isBlank()) {
            throw new IllegalArgumentException("Country ID is required");
        }
        
        List<LeagueDTO> leagues = footballService.getLeagues(countryId);
        return ResponseEntity.ok(leagues);
    }
    
    /**
     * GET /api/teams?league_id={id} - Fetch teams by league.
     * 
     * @param leagueId league ID
     * @return List of teams
     */
    @Operation(summary = "Get teams by league", description = "Fetches all teams in a specific league")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved teams",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = TeamDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid league ID"),
        @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    @GetMapping("/teams")
    public ResponseEntity<List<TeamDTO>> getTeams(
            @Parameter(description = "League ID to fetch teams for", required = true)
            @RequestParam("league_id") String leagueId) {
        
        log.info("GET /api/teams?league_id={} - Fetching teams", leagueId);
        metrics.getTeamsRequestCounter().increment();
        
        if (leagueId == null || leagueId.isBlank()) {
            throw new IllegalArgumentException("League ID is required");
        }
        
        List<TeamDTO> teams = footballService.getTeams(leagueId);
        return ResponseEntity.ok(teams);
    }
    
    /**
     * GET /api/standings?league_id={id}&team_name={name} - Fetch standings with optional team filter.
     * 
     * @param leagueId league ID
     * @param teamName optional team name filter
     * @return List of standings
     */
    @Operation(summary = "Get standings", description = "Fetches league standings, optionally filtered by team name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved standings",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandingDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid parameters"),
        @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    @GetMapping("/standings")
    public ResponseEntity<List<StandingDTO>> getStandings(
            @Parameter(description = "League ID to fetch standings for", required = true)
            @RequestParam("league_id") String leagueId,
            @Parameter(description = "Optional team name filter")
            @RequestParam(value = "team_name", required = false) String teamName) {
        
        log.info("GET /api/standings?league_id={}&team_name={} - Fetching standings", leagueId, teamName);
        metrics.getStandingsRequestCounter().increment();
        
        if (leagueId == null || leagueId.isBlank()) {
            throw new IllegalArgumentException("League ID is required");
        }
        
        List<StandingDTO> standings = footballService.getStandings(leagueId, teamName);
        return ResponseEntity.ok(standings);
    }
    
    /**
     * POST /api/offline-mode - Toggle offline mode.
     * 
     * @param enabled true to enable offline mode
     * @return Success message
     */
    @Operation(summary = "Toggle offline mode", description = "Enable or disable offline mode for cached data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Offline mode toggled successfully")
    })
    @PostMapping("/offline-mode")
    public ResponseEntity<String> toggleOfflineMode(
            @Parameter(description = "Enable offline mode", required = true)
            @RequestParam("enabled") boolean enabled) {
        
        log.info("POST /api/offline-mode?enabled={}", enabled);
        if (enabled) {
            metrics.getOfflineModeEnableCounter().increment();
        } else {
            metrics.getOfflineModeDisableCounter().increment();
        }
        footballService.setOfflineMode(enabled);
        return ResponseEntity.ok("Offline mode " + (enabled ? "enabled" : "disabled"));
    }
    
    /**
     * DELETE /api/cache - Clear all cached data.
     * 
     * @return Success message
     */
    @Operation(summary = "Clear cache", description = "Clears all cached football data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cache cleared successfully")
    })
    @DeleteMapping("/cache")
    public ResponseEntity<String> clearCache() {
        log.info("DELETE /api/cache - Clearing cache");
        metrics.getCacheEvictionCounter().increment();
        footballService.clearCache();
        return ResponseEntity.ok("Cache cleared successfully");
    }
}
