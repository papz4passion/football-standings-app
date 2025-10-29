package com.sapient.football;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main application class for Football Standings Application.
 * This application provides RESTful APIs to fetch football standings data.
 * 
 * @author Sapient Assessment
 * @version 1.0
 */
@SpringBootApplication
@EnableCaching
public class FootballStandingsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootballStandingsApplication.class, args);
	}

}
