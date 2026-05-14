# Course Record (Karton predmeta)

Backend service for managing and publishing **university course records**: courses (with study-program semesters 1–8), professors, students, enrollments, exams, authors, books, and links between courses and books. It exposes a **JWT-protected admin API** for full CRUD and **public read-only** catalog endpoints.

---

## Tech stack

| Area | Choice |
|------|--------|
| Runtime | Java **17** |
| Framework | **Spring Boot 3.3.4** (Web, Validation, Data JPA, Security) |
| Database | **MySQL** (schema managed by **Liquibase**) |
| API docs | **springdoc-openapi** 2.5 (OpenAPI 3 + Swagger UI) |
| Auth | **Spring Security** + **JJWT** 0.12 (HS256 bearer tokens) |
| Tests | JUnit 5, Spring Boot Test, **H2** (integration tests; Liquibase off) |

---

## Requirements

- **JDK 17** (or newer LTS; use a version supported by Spring Boot 3.3.x for production)
- **Maven 3.9+**
- **MySQL 8.x** reachable from the app (local or Docker)
- A MySQL user that can create the database (optional) and run DDL/FKs: JDBC URL may use `createDatabaseIfNotExist=true`

---

## Quick start

1. **Clone / open** this folder (`course-record-backend`) as the project root.

2. **Local configuration** (not committed; see `.gitignore`):

   ```bash
   cp config/application-local.properties.example config/application-local.properties
   ```

   Edit `config/application-local.properties` and set at least:

   - `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`
   - `jwt.secret` (long random secret; HS256)
   - `app.admin.username` / `app.admin.password` (seeded admin; see below)

3. **Run from this directory** (so `optional:file:./config/application-local.properties` resolves):

   ```bash
   mvn spring-boot:run
   ```

   In IntelliJ, set the **working directory** to `course-record-backend` (not the repo parent only).

4. **Defaults** (from `src/main/resources/application.properties` if not overridden):

   - HTTP port **8080** (override with `server.port` in local config)
   - Servlet context path **`/course-record`** — all routes are under that prefix

---

## Default users and seed data

- **Admin (application user)**  
  Created on first startup when the database has **no professors** yet (see `DataSeeder`). Credentials come from:

  - `app.admin.username` / `app.admin.password` in `config/application-local.properties` (recommended), or  
  - Fallback in `application.properties`: **`admin` / `admin`** (change before any real deployment).

  Use **`POST /course-record/api/auth/login`** with JSON `{"username":"…","password":"…"}`; the response includes a JWT. For secured endpoints, send `Authorization: Bearer <token>`.

- **Demo catalog seed** (same first-run path): two professors (Dušan Savić, Miloš Milić), one course *Napredne softverske tehnologije* (code `NST`, ESPB 6, semester **1**).

---

## Useful URLs (with default port and context path)

| What | URL |
|------|-----|
| Swagger UI | http://localhost:8080/course-record/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/course-record/v3/api-docs |
| Login | `POST` http://localhost:8080/course-record/api/auth/login |
| Public catalog (examples) | `GET` …/api/public/courses, …/books, …/authors |

Swagger: use **Authorize** → HTTP bearer scheme **`bearer-jwt`** → paste the raw JWT (or `Bearer <token>` depending on UI; follow the OpenAPI security description).

---

## API conventions

- **Pagination**: list endpoints accept optional `page`, `size`, and `sort` (defaults: page **0**, size **20**, max size **100**). JSON uses Spring Data **`PagedModel`**: array **`content`** plus nested **`page`** (`size`, `number`, `totalElements`, `totalPages`).
- **Security**: `GET /api/public/**`, auth login, and Swagger/OpenAPI paths are public; other routes require **ROLE_ADMIN** JWT.

### CORS (browser / Next.js)

- Property **`app.cors.allowed-origins`**: comma-separated list of allowed browser origins (default in repo: `http://localhost:3000`).
- **`Access-Control-Allow-Credentials`** is **false** by default (JWT is sent via `Authorization` header; no cookie required). Origins are still explicit (not `*`).
- Override in `config/application-local.properties` for staging/production front-end URLs.

---

## Database and migrations

- Schema is applied by **Liquibase** on startup (`spring.liquibase.change-log`).
- If you need a clean slate: drop the MySQL schema/database, then restart (Liquibase + seeder run again).

---

## Tests

```bash
mvn test
```

Uses the `test` profile (H2 in-memory; Liquibase disabled for speed). No MySQL required for `mvn test`.

---

## Project layout (high level)

- `src/main/java` — application code (controllers, services, entities, security, config)
- `src/main/resources` — `application.properties`, Liquibase changelogs
- `config/` — local overrides (`application-local.properties.example`; real file gitignored)

---

## License / ownership

Internal / example project structure; add your own license and contribution guidelines if this becomes a shared repository.
