# $name$

$description$

A Zefir event-sourced backend service.

## Prerequisites

- JDK 21+
- SBT 1.10+
- PostgreSQL 10+

## Running

```bash
# Start PostgreSQL (example with Docker)
docker run -d --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=postgres postgres:16

# Run the service
sbt run
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | /$entity$s/{id} | Create a new $entity$ |
| GET | /$entity$s/{id} | Get a $entity$ by ID |
| PUT | /$entity$s/{id} | Update a $entity$ |
| DELETE | /$entity$s/{id} | Delete a $entity$ |

## Health Endpoints (Port 9095)

| Path | Description |
|------|-------------|
| /ready | Readiness probe |
| /alive | Liveness probe |
| /metrics | Prometheus metrics |

## Configuration

Environment variables:

| Variable | Default | Description |
|----------|---------|-------------|
| API_HOST | 0.0.0.0 | API bind host |
| API_PORT | 8080 | API bind port |
| POSTGRES_HOST | localhost | PostgreSQL host |
| POSTGRES_PORT | 5432 | PostgreSQL port |
| POSTGRES_DATABASE | postgres | Database name |
| POSTGRES_USERNAME | postgres | Database user |
| POSTGRES_PASSWORD | postgres | Database password |
