# Football Standings API - Observability & Monitoring

This guide explains the metrics collection and observability features implemented in the Football Standings API.

## 📊 Metrics Overview

The application exposes comprehensive metrics for monitoring and observability using:
- **Spring Boot Actuator** - Production-ready features
- **Micrometer** - Metrics collection library
- **Prometheus** - Time-series metrics database
- **Grafana** - Metrics visualization

## 🎯 Available Metrics

### 1. **Business Metrics**

#### API Request Counters
- `football.api.requests{endpoint="countries"}` - Total countries API calls
- `football.api.requests{endpoint="leagues"}` - Total leagues API calls
- `football.api.requests{endpoint="teams"}` - Total teams API calls
- `football.api.requests{endpoint="standings"}` - Total standings API calls

#### Cache Metrics
- `football.cache.hits` - Number of cache hits
- `football.cache.misses` - Number of cache misses
- `football.cache.evictions` - Number of cache evictions

#### Error Metrics
- `football.api.errors` - External API errors count
- `football.validation.errors` - Validation errors count

#### Timing Metrics
- `football.external.api.duration` - External API call duration
- `football.cache.operation.duration` - Cache operation duration

#### Feature Metrics
- `football.offline.mode.enabled` - Offline mode enable count
- `football.offline.mode.disabled` - Offline mode disable count

### 2. **System Metrics** (Spring Boot Actuator)

#### JVM Metrics
- `jvm.memory.used` - Memory usage
- `jvm.memory.max` - Maximum memory
- `jvm.gc.pause` - Garbage collection pause time
- `jvm.threads.live` - Live thread count
- `jvm.classes.loaded` - Loaded class count

#### HTTP Metrics
- `http.server.requests` - HTTP request duration and count
  - Tags: `uri`, `method`, `status`, `exception`
  - Percentiles: p50, p95, p99
  - SLO buckets: 50ms, 100ms, 200ms, 500ms, 1s, 2s, 5s

#### System Metrics
- `system.cpu.usage` - CPU usage
- `system.load.average.1m` - System load average
- `process.uptime` - Process uptime

## 🚀 Quick Start

### Option 1: View Metrics Directly

1. **Start the application**:
   ```bash
   cd backend
   gradle bootRun
   ```

2. **Access metrics endpoints**:
   - Health: http://localhost:8080/actuator/health
   - All metrics: http://localhost:8080/actuator/metrics
   - Prometheus format: http://localhost:8080/actuator/prometheus

3. **Query specific metrics**:
   ```bash
   # Cache hits
   curl http://localhost:8080/actuator/metrics/football.cache.hits
   
   # API requests
   curl http://localhost:8080/actuator/metrics/football.api.requests
   
   # HTTP request duration
   curl http://localhost:8080/actuator/metrics/http.server.requests
   ```

### Option 2: Full Observability Stack (Prometheus + Grafana)

1. **Build the Docker image**:
   ```bash
   cd backend
   gradle clean build -x test
   cd ..
   docker build -t football-standings-api:latest -f backend/Dockerfile backend/
   ```

2. **Start the observability stack**:
   ```bash
   docker-compose -f docker-compose-observability.yml up -d
   ```

3. **Access the services**:
   - **API**: http://localhost:8080
   - **Prometheus**: http://localhost:9090
   - **Grafana**: http://localhost:3000 (admin/admin)
   - **Swagger UI**: http://localhost:8080/swagger-ui.html

4. **View metrics in Prometheus**:
   - Open http://localhost:9090
   - Go to "Graph" tab
   - Example queries:
     ```promql
     # Total API requests by endpoint
     sum(football_api_requests_total) by (endpoint)
     
     # Cache hit rate
     rate(football_cache_hits_total[5m]) / 
     (rate(football_cache_hits_total[5m]) + rate(football_cache_misses_total[5m]))
     
     # 95th percentile response time
     histogram_quantile(0.95, 
       rate(http_server_requests_seconds_bucket[5m]))
     
     # Error rate
     rate(football_api_errors_total[5m])
     ```

5. **Create dashboards in Grafana**:
   - Open http://localhost:3000
   - Login with admin/admin
   - Add Prometheus datasource (already provisioned)
   - Create dashboard with panels

## 📈 Example PromQL Queries

### Request Rate
```promql
# Requests per second by endpoint
rate(football_api_requests_total[5m])

# Total requests in last hour
increase(football_api_requests_total[1h])
```

### Cache Performance
```promql
# Cache hit rate percentage
(rate(football_cache_hits_total[5m]) / 
 (rate(football_cache_hits_total[5m]) + rate(football_cache_misses_total[5m]))) * 100

# Cache operations per second
rate(football_cache_hits_total[5m]) + rate(football_cache_misses_total[5m])
```

### Response Time
```promql
# Average response time by URI
rate(http_server_requests_seconds_sum[5m]) / 
rate(http_server_requests_seconds_count[5m])

# 99th percentile response time
histogram_quantile(0.99, 
  rate(http_server_requests_seconds_bucket[5m]))
```

### Error Rate
```promql
# Errors per second
rate(football_api_errors_total[5m])

# HTTP 5xx error rate
rate(http_server_requests_total{status=~"5.."}[5m])

# Error percentage
(rate(http_server_requests_total{status=~"5.."}[5m]) / 
 rate(http_server_requests_total[5m])) * 100
```

### System Health
```promql
# Memory usage percentage
(jvm_memory_used_bytes / jvm_memory_max_bytes) * 100

# CPU usage
system_cpu_usage * 100

# Thread count
jvm_threads_live_threads
```

## 🎨 Grafana Dashboard Examples

### Panel 1: API Request Rate
- Type: Graph
- Query: `rate(football_api_requests_total[5m])`
- Legend: `{{endpoint}}`

### Panel 2: Cache Hit Rate
- Type: Gauge
- Query: Cache hit rate percentage
- Thresholds: Green > 80%, Yellow 50-80%, Red < 50%

### Panel 3: Response Time Percentiles
- Type: Graph
- Queries:
  - p50: `histogram_quantile(0.50, rate(http_server_requests_seconds_bucket[5m]))`
  - p95: `histogram_quantile(0.95, rate(http_server_requests_seconds_bucket[5m]))`
  - p99: `histogram_quantile(0.99, rate(http_server_requests_seconds_bucket[5m]))`

### Panel 4: Error Rate
- Type: Stat
- Query: `rate(football_api_errors_total[5m])`
- Color mode: Value-based (red for errors)

## 📋 Metrics Endpoints

| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Application health status |
| `/actuator/info` | Application information |
| `/actuator/metrics` | List all available metrics |
| `/actuator/metrics/{name}` | Specific metric details |
| `/actuator/prometheus` | Metrics in Prometheus format |
| `/actuator/httptrace` | HTTP request trace |
| `/actuator/threaddump` | Thread dump |

## 🔍 Monitoring Best Practices

1. **Set up alerts** in Prometheus for:
   - High error rates (> 1%)
   - Slow response times (p95 > 2s)
   - Low cache hit rate (< 70%)
   - High memory usage (> 80%)

2. **Monitor key metrics**:
   - API request rate and latency
   - Cache effectiveness
   - External API performance
   - System resources (CPU, memory)

3. **Create dashboards** for:
   - Business metrics (API usage by endpoint)
   - Technical metrics (JVM, HTTP, cache)
   - SLO tracking (uptime, response time, error rate)

## 🛠️ Troubleshooting

### Metrics not showing in Prometheus
1. Check if API is running: `curl http://localhost:8080/actuator/health`
2. Check Prometheus targets: http://localhost:9090/targets
3. Verify Prometheus can reach API: `docker network inspect football-standings-app_monitoring`

### Grafana not showing data
1. Verify datasource connection in Grafana
2. Check if Prometheus has data: http://localhost:9090/graph
3. Test PromQL query in Prometheus first

## 📚 Additional Resources

- [Spring Boot Actuator Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Micrometer Documentation](https://micrometer.io/docs)
- [Prometheus Documentation](https://prometheus.io/docs/)
- [Grafana Documentation](https://grafana.com/docs/)
- [PromQL Guide](https://prometheus.io/docs/prometheus/latest/querying/basics/)

## 🚦 Stopping the Stack

```bash
# Stop all services
docker-compose -f docker-compose-observability.yml down

# Stop and remove volumes
docker-compose -f docker-compose-observability.yml down -v
```
