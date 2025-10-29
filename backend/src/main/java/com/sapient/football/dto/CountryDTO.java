package com.sapient.football.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Country information from external API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDTO {
    
    @JsonProperty("country_id")
    private String countryId;
    
    @JsonProperty("country_name")
    private String countryName;
    
    @JsonProperty("country_logo")
    private String countryLogo;
}
