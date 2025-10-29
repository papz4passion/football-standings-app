package com.sapient.football.service;

import com.sapient.football.dto.CountryDTO;
import com.sapient.football.dto.LeagueDTO;
import com.sapient.football.dto.StandingDTO;
import com.sapient.football.dto.TeamDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Service for making HTTP calls to external Football API.
 * Implements Single Responsibility Principle - handles only API communication.
 */
@Slf4j
@Service
public class FootballApiService {
    
    private final WebClient webClient;
    
    @Value("${api.football.api-key}")
    private String apiKey;
    
    public FootballApiService(WebClient webClient) {
        this.webClient = webClient;
    }
    
    /**
     * Fetches all countries from external API.
     * 
     * @return List of countries
     */
    public List<CountryDTO> getCountries() {
        log.info("Fetching countries from external API");
        
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("action", "get_countries")
                        .queryParam("APIkey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CountryDTO>>() {})
                .doOnError(error -> log.error("Error fetching countries: {}", error.getMessage()))
                .onErrorResume(error -> {
                    log.warn("Returning empty list due to API error");
                    return Mono.just(List.of());
                })
                .block();
    }
    
    /**
     * Fetches leagues for a specific country.
     * 
     * @param countryId country ID
     * @return List of leagues
     */
    public List<LeagueDTO> getLeagues(String countryId) {
        log.info("Fetching leagues for country ID: {}", countryId);
        
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("action", "get_leagues")
                        .queryParam("country_id", countryId)
                        .queryParam("APIkey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<LeagueDTO>>() {})
                .doOnError(error -> log.error("Error fetching leagues: {}", error.getMessage()))
                .onErrorResume(error -> {
                    log.warn("Returning empty list due to API error");
                    return Mono.just(List.of());
                })
                .block();
    }
    
    /**
     * Fetches teams for a specific league.
     * 
     * @param leagueId league ID
     * @return List of teams
     */
    public List<TeamDTO> getTeams(String leagueId) {
        log.info("Fetching teams for league ID: {}", leagueId);
        
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("action", "get_teams")
                        .queryParam("league_id", leagueId)
                        .queryParam("APIkey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<TeamDTO>>() {})
                .doOnError(error -> log.error("Error fetching teams: {}", error.getMessage()))
                .onErrorResume(error -> {
                    log.warn("Returning empty list due to API error");
                    return Mono.just(List.of());
                })
                .block();
    }
    
    /**
     * Fetches standings for a specific league.
     * 
     * @param leagueId league ID
     * @return List of standings
     */
    public List<StandingDTO> getStandings(String leagueId) {
        log.info("Fetching standings for league ID: {}", leagueId);
        
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("action", "get_standings")
                        .queryParam("league_id", leagueId)
                        .queryParam("APIkey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<StandingDTO>>() {})
                .doOnError(error -> log.error("Error fetching standings: {}", error.getMessage()))
                .onErrorResume(error -> {
                    log.warn("Returning empty list due to API error");
                    return Mono.just(List.of());
                })
                .block();
    }
}
