package com.sapient.football.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Venue information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueDTO {
    
    @JsonProperty("venue_name")
    private String venueName;
    
    @JsonProperty("venue_address")
    private String venueAddress;
    
    @JsonProperty("venue_city")
    private String venueCity;
    
    @JsonProperty("venue_capacity")
    private String venueCapacity;
    
    @JsonProperty("venue_surface")
    private String venueSurface;
}
