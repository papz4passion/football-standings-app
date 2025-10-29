package com.sapient.football.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health check controller.
 */
@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "Health Check", description = "Health check endpoints")
public class HealthController {
    
    @Operation(summary = "Health check", description = "Check if the service is running")
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        log.debug("Health check requested");
        
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "Football Standings API");
        health.put("version", "1.0.0");
        
        return ResponseEntity.ok(health);
    }
}
