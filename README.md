# WastePickup - Waste Collection Schedule & Segregation Score Tracker

Spring Boot 3 + Spring Data JPA + MySQL.

## Run
1. Install JDK 17+, Maven, MySQL.
2. Edit `src/main/resources/application.properties` (MySQL username/password).
   The database `wastepickup` is created automatically.
3. `mvn spring-boot:run`
4. Swagger UI: http://localhost:8080/swagger-ui.html

## Config (application.properties)
- `waste.min-score`      households whose rolling average is below this are flagged (default 50)
- `waste.rolling-window` number of latest pickups used for the rolling average (default 5)

## Endpoints
| Method | URL | Purpose |
|---|---|---|
| POST/GET | /api/zones | create / list zones |
| GET/PUT/DELETE | /api/zones/{id} | read / update / delete zone |
| POST/GET | /api/zones/{id}/schedules | add / list schedules of a zone |
| GET/PUT/DELETE | /api/schedules/{id} | read / update / delete schedule |
| POST/GET | /api/households | create / list households |
| GET/PUT/DELETE | /api/households/{id} | read / update / delete household |
| GET | /api/households/flagged | households needing a reminder notice |
| GET | /api/households/{id}/pickups | pickup history |
| POST | /api/pickups | log a pickup (validated against zone window) |
| GET | /api/zones/average-scores | zone-wise average score (all zones) |
| GET | /api/zones/{id}/average-score | average score of one zone |

## Quick test flow (Postman)
1. POST /api/zones                 {"name":"Zone A","description":"North"}
2. POST /api/zones/1/schedules     {"dayOfWeek":"MONDAY","startTime":"07:00:00","endTime":"10:00:00"}
3. POST /api/households            {"ownerName":"Ravi","address":"12 Main St","zoneId":1}
4. POST /api/pickups               {"householdId":1,"collectorName":"Kumar","score":40,"pickupTime":"2026-09-28T08:00:00"}   (a Monday -> 201, household flagged)
5. POST /api/pickups               {"householdId":1,"collectorName":"Kumar","score":40,"pickupTime":"2026-09-28T15:00:00"}   (outside window -> 422)
6. GET  /api/households/flagged
7. GET  /api/zones/1/average-score

`pickupTime` is optional; if omitted the server's current time is used.

## Error format
{ "timestamp": "...", "status": 422, "error": "Unprocessable Entity", "message": "..." }
400 validation / bad JSON, 404 not found, 409 duplicate, 422 business rule violated.

## Layers
controller -> service (business rules) -> repository -> MySQL
