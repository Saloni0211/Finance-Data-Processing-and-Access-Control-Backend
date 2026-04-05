# Finance Data Processing & Access Control (Java / Spring Boot)

A compact backend for a finance dashboard that demonstrates role-based access control, financial record CRUD, and summary endpoints. Built with Spring Boot 3, Spring Security, Spring Data JPA, and an in-memory H2 database for simplicity.

## Quick start
1) Prerequisites: Java 17+ and Maven.
2) Run (no Maven install needed):
```bash
./mvnw spring-boot:run
```
3) Default admin user (auto-seeded):
   - username: `admin`
   - password: `admin123`
4) H2 console (dev only): http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:financedb`, user `sa`, password empty).
5) Built-in simple web UI: http://localhost:8080 (served from `src/main/resources/static/index.html`).
   - Login, view summary/records, create/delete records.
   - Register new users (needs admin creds) and reset passwords (admin).
6) API docs/UI: http://localhost:8080/swagger-ui.html (OpenAPI served from `/v3/api-docs`).

## Domain & roles
- Roles: `ADMIN` (manage users + records), `ANALYST` (read records + summaries), `VIEWER` (read-only summaries/records).
- User status: `ACTIVE` or `INACTIVE` (inactive users cannot authenticate).
- Financial record: `amount`, `type` (`INCOME|EXPENSE`), `category`, `date`, optional `notes`, audit fields, and `createdBy`.

## Access control
Spring Security + method-level `@PreAuthorize`:
- `/users/**`: ADMIN only.
- `/records` GET: ADMIN, ANALYST, VIEWER.
- `/records` POST/PUT/DELETE: ADMIN only.
- `/dashboard/summary`: ADMIN, ANALYST, VIEWER.
HTTP Basic auth is used for clarity in the exercise; swap to JWT/session as needed.

## APIs (happy-path examples)
All endpoints are under `http://localhost:8080` and expect Basic auth.

### Users
- `POST /users` — create user. Body: `{ "username": "analyst1", "password": "secret123", "role": "ANALYST", "status": "ACTIVE" }`
- `GET /users` — list users.
- `PUT /users/{id}` — update role/status/password.

### Records
- `GET /records?type=EXPENSE&category=Groceries&startDate=2024-01-01&endDate=2024-12-31`
- `POST /records` — create (ADMIN). Body: `{ "amount": 120.50, "type": "EXPENSE", "category": "Groceries", "date": "2026-04-01", "notes": "Weekly shop" }`
- `PUT /records/{id}` — update (ADMIN).
- `DELETE /records/{id}` — delete (ADMIN).

### Dashboard
- `GET /dashboard/summary` — returns totals (income, expense, net), category breakdowns, 6-month trend, and last 10 records.

## Validation & errors
- Bean validation on payloads (amount > 0, required fields, max lengths).
- Consistent error shape: `{ "timestamp": "...", "message": "...", "path": "/api/path" }` with appropriate HTTP status codes (400/403/404/409/500).

## Data & persistence
- H2 in-memory DB (`spring.jpa.hibernate.ddl-auto=update`). Swap to Postgres/MySQL by adjusting `application.properties` datasource settings.
- Sample records seeded with the default admin to make the dashboard non-empty.

## Notes and assumptions
- Basic auth is acceptable for the assignment; production should use JWT/OAuth2 and HTTPS.
- Roles kept minimal per assignment; extend with granular permissions if needed.
- No pagination added to keep the sample focused; easy to add via Spring Data `Pageable` on record listing.
- Tests not included due to time; recommended to add service-level and controller slice tests.
- Minimal frontend included at `/` to exercise core flows without extra tooling.

## Package layout (feature-first)
- `common` — config, security, exception handling, shared DTOs.
- `user` — user entity/enums, DTOs, repo, service, controller.
- `record` — financial record entity/enums, DTOs, repo, service, controller.
- `dashboard` — summary DTOs, service, controller.
