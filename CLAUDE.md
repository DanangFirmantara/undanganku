# CLAUDE.md — `undanganku`

This is **Undanganku**, a wedding invitation web application. Identity is self-contained — this app owns its own JWT auth, user management, and all business logic.

- Java 21, Spring Boot 3.3, WAR on JBoss EAP 8
- Angular 18 standalone, Tailwind CSS, Signals
- PostgreSQL 16 only; Flyway migrations in `db/migration/postgresql/`
- No Lombok — explicit getters/setters/constructors

---

| Owns |
|---|
| JWT issuance & validation (JJWT 0.12.3, HS256, 15-min expiry) |
| User entity, roles (ROLE_ADMIN / ROLE_USER), login/logout pages |
| All business tables, APIs, and audit log |

**Default credentials (dev only):** `admin@app.com / Admin@123456` (ROLE_ADMIN), `user@app.com / User@123456` (ROLE_USER)

**Required env var:** `JWT_SECRET`

---

## Auth Model

JWT stored in `localStorage` (`auth_token`, `auth_user`, `auth_expires`). Frontend attaches `Authorization: Bearer <token>` on every request except `/api/auth/login`. Auto-logout fires when token expires. Backend validates via `JwtAuthFilter`. Use `hasRole("ADMIN")`, not `hasRole("ROLE_ADMIN")`.

---

## Non-Negotiable Rules

- Every table: `id`, `guid` (UUID), `created_at`, `created_by`, `updated_at`, `updated_by`, `deleted_at`
- Soft-delete: `deleted_at IS NULL` — never hard delete
- No Oracle — PostgreSQL 16 only, never write to `db/migration/oracle/`
- Never edit a committed Flyway migration file — add a new version
- `ChangeDetectionStrategy.OnPush` on all Angular components
- `@if` / `@for` / `@switch` control flow — no `*ngIf` / `*ngFor`
- Standalone Angular components — no NgModules

---

## Agent Rules

For backend (Spring Boot, JWT, security, API):
→ Read `.claude/agents/java-backend-agent.md` first, follow `## Issue Resolution Protocol`

For frontend (Angular, components, routing, Tailwind):
→ Read `.claude/agents/angular-agent.md` first, follow `## Issue Resolution Protocol`

For database (Flyway, migrations, schema):
→ Read `.claude/agents/backend-agent.md` first, follow `## Issue Resolution Protocol`

After every task: create/update lesson files in `backend/lessons/` or `frontend/lessons/` and register them in the agent file.
