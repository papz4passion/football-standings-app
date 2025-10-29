package com.sapient.football.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Configuration for custom application metrics.
 * Provides business-specific metrics for monitoring.
 */
@Configuration
public class MetricsConfig {
    
    /**
     * Custom metrics holder for football API operations.
     */
    @Component
    @Getter
    public static class FootballMetrics {
        
        // API Call Counters
        private final Counter countriesRequestCounter;
        private final Counter leaguesRequestCounter;
        private final Counter teamsRequestCounter;
        private final Counter standingsRequestCounter;
        
        // Cache Metrics
        private final Counter cacheHitCounter;
        private final Counter cacheMissCounter;
        private final Counter cacheEvictionCounter;
        
        // Error Counters
        private final Counter apiErrorCounter;
        private final Counter validationErrorCounter;
        
        // Timers for API calls
        private final Timer externalApiTimer;
        private final Timer cacheOperationTimer;
        
        // Offline Mode Metrics
        private final Counter offlineModeEnableCounter;
        private final Counter offlineModeDisableCounter;
        
        public FootballMetrics(MeterRegistry registry) {
            // Initialize request counters
            this.countriesRequestCounter = Counter.builder("football.api.requests")
                    .tag("endpoint", "countries")
                    .description("Number of requests to countries endpoint")
                    .register(registry);
            
            this.leaguesRequestCounter = Counter.builder("football.api.requests")
                    .tag("endpoint", "leagues")
                    .description("Number of requests to leagues endpoint")
                    .register(registry);
            
            this.teamsRequestCounter = Counter.builder("football.api.requests")
                    .tag("endpoint", "teams")
                    .description("Number of requests to teams endpoint")
                    .register(registry);
            
            this.standingsRequestCounter = Counter.builder("football.api.requests")
                    .tag("endpoint", "standings")
                    .description("Number of requests to standings endpoint")
                    .register(registry);
            
            // Initialize cache counters
            this.cacheHitCounter = Counter.builder("football.cache.hits")
                    .description("Number of cache hits")
                    .register(registry);
            
            this.cacheMissCounter = Counter.builder("football.cache.misses")
                    .description("Number of cache misses")
                    .register(registry);
            
            this.cacheEvictionCounter = Counter.builder("football.cache.evictions")
                    .description("Number of cache evictions")
                    .register(registry);
            
            // Initialize error counters
            this.apiErrorCounter = Counter.builder("football.api.errors")
                    .description("Number of external API errors")
                    .register(registry);
            
            this.validationErrorCounter = Counter.builder("football.validation.errors")
                    .description("Number of validation errors")
                    .register(registry);
            
            // Initialize timers
            this.externalApiTimer = Timer.builder("football.external.api.duration")
                    .description("Time taken for external API calls")
                    .register(registry);
            
            this.cacheOperationTimer = Timer.builder("football.cache.operation.duration")
                    .description("Time taken for cache operations")
                    .register(registry);
            
            // Initialize offline mode counters
            this.offlineModeEnableCounter = Counter.builder("football.offline.mode.enabled")
                    .description("Number of times offline mode was enabled")
                    .register(registry);
            
            this.offlineModeDisableCounter = Counter.builder("football.offline.mode.disabled")
                    .description("Number of times offline mode was disabled")
                    .register(registry);
        }
    }
}
