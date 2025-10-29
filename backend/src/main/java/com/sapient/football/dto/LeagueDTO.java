package com.sapient.football.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for League information from external API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeagueDTO {
    
    @JsonProperty("country_id")
    private String countryId;
    
    @JsonProperty("country_name")
    private String countryName;
    
    @JsonProperty("league_id")
    private String leagueId;
    
    @JsonProperty("league_name")
    private String leagueName;
    
    @JsonProperty("league_season")
    private String leagueSeason;
    
    @JsonProperty("league_logo")
    private String leagueLogo;
    
    @JsonProperty("country_logo")
    private String countryLogo;
}
