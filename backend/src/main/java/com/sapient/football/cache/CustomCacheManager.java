package com.sapient.football.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Generic cache manager with TTL support and thread-safe operations.
 * Provides in-memory caching with automatic expiration.
 */
@Slf4j
@Component
public class CustomCacheManager<K, V> {
    
    private final Map<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();
    
    /**
     * Stores a value in cache with TTL.
     * 
     * @param key cache key
     * @param value value to cache
     * @param ttlMillis time-to-live in milliseconds
     */
    public void put(K key, V value, long ttlMillis) {
        CacheEntry<V> entry = new CacheEntry<>(value, System.currentTimeMillis() + ttlMillis);
        cache.put(key, entry);
        log.debug("Cached entry with key: {} (TTL: {}ms)", key, ttlMillis);
    }
    
    /**
     * Retrieves a value from cache if not expired.
     * 
     * @param key cache key
     * @return Optional containing the cached value if present and not expired
     */
    public Optional<V> get(K key) {
        CacheEntry<V> entry = cache.get(key);
        
        if (entry == null) {
            log.debug("Cache miss for key: {}", key);
            return Optional.empty();
        }
        
        if (entry.isExpired()) {
            cache.remove(key);
            log.debug("Cache entry expired for key: {}", key);
            return Optional.empty();
        }
        
        log.debug("Cache hit for key: {}", key);
        return Optional.of(entry.getValue());
    }
    
    /**
     * Checks if cache contains a non-expired entry for the given key.
     * 
     * @param key cache key
     * @return true if cache contains non-expired entry
     */
    public boolean contains(K key) {
        return get(key).isPresent();
    }
    
    /**
     * Clears all entries from cache.
     */
    public void clear() {
        cache.clear();
        log.info("Cache cleared");
    }
    
    /**
     * Removes expired entries from cache.
     * 
     * @return number of entries removed
     */
    public int evictExpired() {
        int count = 0;
        for (Map.Entry<K, CacheEntry<V>> entry : cache.entrySet()) {
            if (entry.getValue().isExpired()) {
                cache.remove(entry.getKey());
                count++;
            }
        }
        if (count > 0) {
            log.info("Evicted {} expired cache entries", count);
        }
        return count;
    }
    
    /**
     * Gets the size of the cache.
     * 
     * @return number of entries in cache
     */
    public int size() {
        return cache.size();
    }
    
    /**
     * Inner class representing a cache entry with expiration time.
     */
    private static class CacheEntry<V> {
        private final V value;
        private final long expiryTime;
        
        public CacheEntry(V value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }
        
        public V getValue() {
            return value;
        }
        
        public boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }
}
