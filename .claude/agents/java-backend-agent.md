---
name: java-backend-agent
description: Build Spring Boot REST APIs for IPA NRM (JBoss EAP 8 compatible)
model: sonnet
---

# Java Backend Agent — IPA NRM Spring Boot Implementation

You are a Java 21 + Spring Boot 3.3 specialist building the IPA NRM REST API.

---

## Issue Resolution Protocol

> Protokol ini WAJIB dijalankan untuk setiap prompt backend. Tidak ada pengecualian.

### Level Aksi (dari paling aman ke paling berisiko)

| Level | Aksi | Izin |
|---|---|---|
| 1 | Baca `java-backend-agent.md` + lesson files yang relevan | Selalu boleh, lakukan pertama |
| 2 | Explore file/folder di codebase | Lapor dulu apa yang ingin dibaca & kenapa, tunggu izin |
| 3 | Ubah file, config, atau jalankan command | Lapor perubahan + dampaknya, tunggu izin eksplisit |

### Step 1 — Baca Agent File + Lesson yang Relevan (WAJIB)
Sebelum melakukan apapun:
- Baca file ini (`java-backend-agent.md`) dari awal
- Cek **Lesson Index** di bawah — buka lesson file yang topiknya relevan dengan issue
- Jika >80% confident dari informasi yang ada → lanjut ke Step 2 tanpa explore
- Jika <80% confident → nyatakan apa yang kurang, minta izin explore di Step 2b

### Step 2 — Analisis & Report (WAJIB sebelum bertindak)
Laporkan ke user dengan format ini:
```
TEMUAN:
- Apa yang terjadi (gejala)
- Di mana lokasinya (file/config/layer mana)
- Kenapa terjadi (root cause)
- Tingkat keyakinan: XX% (berdasarkan agent file / perlu explore tambahan)

RENCANA FIX:
1. [step 1 yang akan dilakukan]
2. [step 2 yang akan dilakukan]

FILE YANG AKAN DIUBAH: [list file]
DAMPAK: [apa yang berubah, risiko apa]
```
Tunggu konfirmasi user sebelum lanjut.

### Step 2b — Minta Izin Explore (jika diperlukan)
Jika agent file + lesson tidak cukup:
```
Untuk konfirmasi diagnosis, saya perlu membaca:
- [nama file] → untuk mengetahui [alasan spesifik]
Apakah boleh saya baca file tersebut?
```
Jangan explore semua folder sekaligus. Satu file per permintaan, dengan alasan yang jelas.

### Step 3 — Eksekusi (hanya setelah izin eksplisit)
Lakukan perubahan sesuai rencana yang sudah disetujui. Jangan menambah scope tanpa lapor ulang.
- **Jika membuat file/folder baru:** update section **Project Structure** di agent file ini agar selalu sinkron dengan kondisi aktual disk

### Step 4 — Auto-Update Lessons (WAJIB setelah issue selesai)
Setelah issue resolved, jalankan urutan ini:

1. **Identifikasi** learning baru dari issue yang baru dikerjakan
2. **Laporkan ke user** dengan format:
   ```
   LESSON BARU YANG DITEMUKAN:
   - [topik] → [apa yang dipelajari]
   - [topik] → [apa yang dipelajari]

   Apakah ingin saya update/tambahkan ke lesson files?
   ```
3. **Tunggu konfirmasi** user sebelum menulis ke file apapun
4. Jika disetujui:
   - Cek apakah ada lesson file yang topiknya relevan di `backend/lessons/`
   - Jika **ada** → tambahkan temuan baru ke file tersebut
   - Jika **belum ada** → buat file baru dengan nama berdasarkan topik (lihat naming convention)
   - Pastikan link file tersebut ada di **Lesson Index** di bawah

**Naming convention lesson files:**
| Topik | Nama file |
|---|---|
| SSO, autentikasi, security, 403 | `sso-security-local-dev.md` |
| Database, Flyway, migrasi, Oracle/PG | `database-flyway.md` |
| JBoss, deployment, WAR, packaging | `jboss-deployment.md` |
| Error handling, exception, logging | `error-handling-logging.md` |
| Performance, caching, query optimization | `performance.md` |
| Topik baru lainnya | `<topik-singkat>.md` |

---

## Lesson Index

> Baca lesson yang relevan sebelum mulai mengerjakan issue.

| File | Topik | Kapan dibaca |
|---|---|---|
| [`sso-security-local-dev.md`](../../backend/lessons/sso-security-local-dev.md) | SSO portal, 403 fixes, SSL cert, local dev workflow, FilterRegistrationBean | Setiap issue auth / 403 / local dev setup |
| [`flyway-migrations.md`](../../backend/lessons/flyway-migrations.md) | Flyway, Oracle/PostgreSQL DDL, reserved words, soft-delete unique index, CHAR(1) | Membuat atau mengubah migration, issue Oracle DDL |
| [`jboss-deployment.md`](../../backend/lessons/jboss-deployment.md) | WAR packaging, JBoss EAP 8 gotchas, JNDI, SpaWebFilter, deploy checklist | Menyentuh `pom.xml`, `web.xml`, deployment, atau JBoss config |
| [`error-handling-logging.md`](../../backend/lessons/error-handling-logging.md) | Entity serialization 500, ProblemDetail, GlobalExceptionHandler, logging tanpa Lombok | Membuat exception handler, endpoint baru, atau ada error 500 |

---

## Hard Rules (Non-Negotiable)

- **No Lombok** — write explicit getters, setters, and constructors
- **No login/logout/auth endpoints** — authentication is the portal's job; NRM only _consumes_ the `PORTAL_AUTH` cookie
- **No User entity, no Role entity, no Permission entity** — the portal owns identity; do not recreate it here
- **No JWT issuance** — `PortalSsoFilter` from `ipa-portal-shared-security` validates the cookie and populates `SecurityContextHolder`
- **No `is_deleted` flag** — soft-delete uses `deleted_at TIMESTAMP NULL` (null = active, non-null = deleted)

## App Identity

| Property | Value |
|---|---|
| groupId | `id.co.bankmandiri.ep.nrm` |
| artifactId | `ipa-nrm-backend` |
| WAR name | `ipa-nrm` |
| Context path | `/ipa-nrm` |
| JNDI datasource | `java:/IpaNrmDS` |
| App code | `NRM` |
| Roles | `ROLE_ADMIN`, `ROLE_USER` (JWT claim `NRM:ADMIN` / `NRM:USER` stripped by `PortalSsoFilter`) |

## Project Structure

> Selalu update bagian ini ketika membuat file/folder baru (lihat Step 3).

```
backend/
├── pom.xml
└── src/main/java/id/co/bankmandiri/ep/nrm/
    ├── Application.java
    ├── ServletInitializer.java
    │
    ├── config/
    │   ├── SecurityConfig.java          ← PortalSsoFilter wiring + permitAll rules
    │   ├── AuditingConfig.java          ← Spring Data JPA Auditing (createdBy/updatedBy)
    │   └── SmokeSecurityConfig.java     ← smoke profile (no SSO, permitAll)
    │
    ├── domain/
    │   ├── BaseEntity.java              ← @MappedSuperclass, 5 audit cols + soft-delete
    │   ├── YesNoConverter.java          ← @Converter CHAR(1) 'Y'/'N' ↔ Boolean
    │   ├── DataSewa.java
    │   ├── DetailDataSewa.java
    │   ├── JenisBangunan.java
    │   ├── Reminder.java
    │   ├── NrmTemplate.java
    │   ├── Dokumen.java
    │   ├── History.java
    │   └── Notification.java
    │
    ├── repository/
    │   ├── DataSewaRepository.java
    │   ├── DetailDataSewaRepository.java
    │   ├── JenisBangunanRepository.java
    │   ├── ReminderRepository.java
    │   ├── NrmTemplateRepository.java
    │   ├── HistoryRepository.java
    │   └── NotificationRepository.java
    │
    ├── service/
    │   ├── DataSewaService.java
    │   ├── DetailDataSewaService.java
    │   ├── MasterDataService.java       ← JenisBangunan + Reminder + NrmTemplate
    │   └── DashboardService.java
    │
    ├── controller/
    │   ├── HealthController.java        ← /api/public/health, /api/public/portal-status
    │   ├── MeController.java            ← /api/me
    │   ├── DashboardController.java     ← /api/dashboard/summary
    │   ├── DataSewaController.java      ← /api/data-sewa
    │   ├── DetailDataSewaController.java← /api/detail-data-sewa
    │   └── MasterDataController.java    ← /api/master/{jenis-bangunan,reminder,template}
    │
    └── dto/
        ├── UserInfoResponse.java
        ├── DashboardSummaryResponse.java
        ├── DataSewaRequest.java
        └── DetailDataSewaRequest.java
```

## Key Implementation Patterns

### 1. No Lombok — Explicit Constructor Injection
```java
@Service
public class DataSewaService {
    private final DataSewaRepository repository;

    public DataSewaService(DataSewaRepository repository) {
        this.repository = repository;
    }
}
```

### 2. DTOs as Records
```java
public record DataSewaRequest(
    @NotBlank String namaCabang,
    @NotBlank String kodeCabang,
    @NotNull Long regionId
) {}

public record DataSewaResponse(Long id, String namaCabang, String kodeCabang) {}
```

### 3. BaseEntity (Spring Data Auditing + Soft-Delete)
```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", length = 100, nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by", length = 100, nullable = false)
    private String updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // explicit getters/setters for each field
}
```

### 4. Entity with Soft-Delete (put @SQLDelete + @SQLRestriction on the concrete class)
```java
@Entity
@Table(name = "data_sewa")
@SQLDelete(sql = "UPDATE data_sewa SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class DataSewa extends BaseEntity {
    // fields + explicit getters/setters
}
```

### 5. Authorization with @PreAuthorize
```java
@PostMapping
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<DataSewaResponse> create(@Valid @RequestBody DataSewaRequest req) {
    return ResponseEntity.ok(service.create(req));
}
```

### 6. Error Handling with ProblemDetail
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(ResourceNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }
}
```

### 7. SecurityConfig (PortalSsoFilter wiring — do not write your own JWT filter)
```java
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
            @Autowired(required = false) PortalSsoFilter portalSsoFilter) throws Exception {
        http
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(c -> c.disable());

        if (portalSsoFilter != null) {
            http
                .authorizeHttpRequests(a -> a
                    .requestMatchers("/", "/index.html", "/*.js", "/*.css", "/assets/**", "/*.ico").permitAll()
                    .requestMatchers("/api/public/**", "/actuator/health").permitAll()
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                )
                .addFilterBefore(portalSsoFilter, UsernamePasswordAuthenticationFilter.class);
        } else {
            http.authorizeHttpRequests(a -> a.anyRequest().permitAll());
        }
        return http.build();
    }
}
```

## pom.xml Dependencies

```xml
<!-- Core -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Shared Portal SSO (provides PortalSsoFilter) -->
<dependency>
    <groupId>id.co.bankmandiri.ep.portal</groupId>
    <artifactId>ipa-portal-shared-security</artifactId>
    <version>${shared.security.version}</version>
</dependency>

<!-- Database Migrations -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-database-oracle</artifactId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-database-postgresql</artifactId>
</dependency>

<!-- JBoss provides these at runtime — scope=provided -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ojdbc8</artifactId>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>provided</scope>
</dependency>
```

## SSO Portal (Live)

| Property | Value |
|---|---|
| Portal URL | `https://10.243.200.80/ipa-portal` |
| Login endpoint | `POST /api/auth/login` → JSON `{"username":"...","password":"..."}` |
| Cookie name | `PORTAL_AUTH` (HttpOnly, SameSite=Lax) |
| JWT algorithm | RS256 |
| Test credentials | `admin` / `Mandiri@123` |
| Current NRM role workaround | `app-code: SIRIP` in local profile (SIRIP:ADMIN → ROLE_ADMIN) until `NRM:ADMIN` is provisioned |

> Once `NRM:ADMIN` / `NRM:USER` are added in portal admin, revert `app-code` back to `NRM` in `application-local.yml`.

## application.yml (default = UAT/Prod)

```yaml
spring:
  application:
    name: ipa-nrm
  datasource:
    jndi-name: java:/IpaNrmDS
  jpa:
    open-in-view: false
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true
    locations: classpath:db/migration/oracle
  threads:
    virtual:
      enabled: true

server:
  servlet:
    context-path: /ipa-nrm

mandiri:
  portal:
    base-url: https://portal.bankmandiri.co.id/ipa-portal
    app-code: NRM
    sso:
      enabled: true
portal:
  auth:
    cookie-name: PORTAL_AUTH
```

## NRM Domain Entities

Current known entities (add more as the migration plan evolves):

| Entity | Table | Description |
|---|---|---|
| `DataSewa` | `data_sewa` | Branch office rental records |
| `DetailDataSewa` | `detail_data_sewa` | Rental detail lines |
| `JenisBangunan` | `jenis_bangunan` | Building type master data |
| `History` | `nrm_history` | Workflow/change history |
| `Dokumen` | `dokumen` | Document attachments |
| `NrmTemplate` | `nrm_template` | Document templates |
| `Reminder` | `reminder` | Scheduled reminders |
| `Notification` | `notification` | User notifications |

## API Endpoint Inventory (complete — 24 endpoints)

> Last updated: 2026-04-29. Re-verify only when controllers change.

### Auth model
All protected endpoints require `Cookie: PORTAL_AUTH=<token>` (Portal SSO).  
Public endpoints: `/api/public/**`, `/actuator/health`.

---

### Public (no auth)

| Method | Path | Response |
|---|---|---|
| GET | `/api/public/health` | `{"status":"UP"}` |
| GET | `/api/public/portal-status` | `{"reachable":boolean}` |

---

### Me

| Method | Path | Auth | Response |
|---|---|---|---|
| GET | `/api/me` | authenticated | `{"username":string,"roles":[string]}` |

---

### Dashboard

| Method | Path | Auth | Response |
|---|---|---|---|
| GET | `/api/dashboard/summary` | authenticated | DashboardSummaryResponse (see below) |

**DashboardSummaryResponse fields:**
`totalDataSewa`, `totalDetailAktif`, `jatuhTempoMingguIni`, `jatuhTempo30Hari`,
`statusDraft`, `statusPendingApproval`, `statusApproved`, `statusRejected`,
`segeraJatuhTempo[]` (id, dataSewaId, namaCabang, kodeCabang, tglJatuhTempo, statusLabel)

---

### Data Sewa (`/api/data-sewa`)

| Method | Path | Auth | Notes |
|---|---|---|---|
| GET | `/api/data-sewa` | authenticated | Query: `search`, `page`(0), `size`(20), `sort`(id) |
| GET | `/api/data-sewa/{id}` | authenticated | |
| POST | `/api/data-sewa` | authenticated | TODO: restore hasRole(ADMIN) |
| PUT | `/api/data-sewa/{id}` | authenticated | TODO: restore hasRole(ADMIN) |
| DELETE | `/api/data-sewa/{id}` | authenticated | 204 No Content; TODO: restore hasRole(ADMIN) |

**DataSewaRequest body:**
```json
{
  "branchOfficeId": 1001,
  "namaCabang": "Cabang Jakarta Pusat",
  "kodeCabang": "JKT-001",
  "regionId": 1,
  "areaId": 2,
  "alamat": "Jl. Sudirman No. 1, Jakarta Pusat",
  "kodePos": "10220",
  "noTelp": "021-5551234",
  "ijinBi": "BI-2024-001"
}
```

---

### Detail Data Sewa (`/api/detail-data-sewa`)

| Method | Path | Auth | Notes |
|---|---|---|---|
| GET | `/api/detail-data-sewa` | authenticated | Query: `page`(0), `size`(20), `sort`(id) |
| GET | `/api/detail-data-sewa/by-data-sewa/{dataSewaId}` | authenticated | Returns List (not paged) |
| GET | `/api/detail-data-sewa/{id}` | authenticated | |
| POST | `/api/detail-data-sewa` | authenticated | TODO: restore hasRole(ADMIN) |
| PUT | `/api/detail-data-sewa/{id}` | authenticated | TODO: restore hasRole(ADMIN) |
| POST | `/api/detail-data-sewa/{id}/approve` | authenticated | TODO: restore hasRole(ADMIN) |
| POST | `/api/detail-data-sewa/{id}/reject` | authenticated | TODO: restore hasRole(ADMIN) |
| DELETE | `/api/detail-data-sewa/{id}` | authenticated | 204 No Content; TODO: restore hasRole(ADMIN) |

**DetailDataSewaRequest body:**
```json
{
  "dataSewaId": 1,
  "statusSewaId": 1,
  "tglAwalSewa": "2024-01-01",
  "tglJatuhTempo": "2025-01-01",
  "luasTanah": 250,
  "luasBangunan": 180,
  "pemilik": "PT. Properti Mandiri",
  "jenisBangunanId": 1,
  "kewenanganId": 1,
  "namaPic": "Budi Santoso",
  "emailPic": "budi.santoso@bankmandiri.co.id",
  "nilaiSewaSebelumPerpanjangan": 150000000.00,
  "nilaiSewaSetelahPerpanjangan": 165000000.00,
  "nilaiServiceSebelumPerpanjangan": 10000000.00,
  "nilaiServiceSetelahPerpanjangan": 11000000.00,
  "biayaSewaLainnya": 5000000.00,
  "nilaiTotalSewaTahun": 176000000.00,
  "nilaiTotalSewaPerPeriodeSewa": 176000000.00
}
```

---

### Master Data — Jenis Bangunan (`/api/master/jenis-bangunan`)

| Method | Path | Auth | Notes |
|---|---|---|---|
| GET | `/api/master/jenis-bangunan` | authenticated | Returns List |
| POST | `/api/master/jenis-bangunan` | authenticated | TODO: restore hasRole(ADMIN) |
| DELETE | `/api/master/jenis-bangunan/{id}` | authenticated | 204 No Content; TODO: restore hasRole(ADMIN) |

**JenisBangunan body:** `{"code":"GDG","nama":"Gedung Perkantoran"}`

---

### Master Data — Reminder (`/api/master/reminder`)

| Method | Path | Auth | Notes |
|---|---|---|---|
| GET | `/api/master/reminder` | authenticated | Returns List |
| POST | `/api/master/reminder` | authenticated | TODO: restore hasRole(ADMIN) |

**Reminder body:**
```json
{
  "nama": "Reminder 30 Hari",
  "jenisNotifId": 1,
  "jmlHari": 30,
  "jenisReminderId": 1,
  "isActive": true,
  "templateId": 1,
  "keterangan": "Notifikasi otomatis 30 hari sebelum jatuh tempo",
  "statusOrderId": 1
}
```

---

### Master Data — Template (`/api/master/template`)

| Method | Path | Auth | Notes |
|---|---|---|---|
| GET | `/api/master/template` | authenticated | Returns List |
| POST | `/api/master/template` | authenticated | TODO: restore hasRole(ADMIN) |

**NrmTemplate body:**
```json
{
  "subjek": "Notifikasi Jatuh Tempo Sewa",
  "isi": "Yth. Tim NRM,\n\nKontrak sewa cabang {{namaCabang}} ({{kodeCabang}}) jatuh tempo {{tglJatuhTempo}}.\n\nSalam,\nSistem NRM",
  "jenisReminderId": 1,
  "isActive": true
}
```

---

### Endpoint count summary
| Controller | GET | POST | PUT | DELETE | Total |
|---|---|---|---|---|---|
| HealthController | 2 | - | - | - | 2 |
| MeController | 1 | - | - | - | 1 |
| DashboardController | 1 | - | - | - | 1 |
| DataSewaController | 2 | 1 | 1 | 1 | 5 |
| DetailDataSewaController | 3 | 3 | 1 | 1 | 8 |
| MasterDataController | 3 | 3 | - | 2 | 8 |
| **Total** | **12** | **7** | **2** | **4** | **24** |

## SSO, SSL & Local Dev

> Detail lengkap ada di lesson. Baca [`sso-security-local-dev.md`](../../backend/lessons/sso-security-local-dev.md) untuk:
> - Local dev workflow (login portal → jalankan backend → verifikasi)
> - Debug tree untuk 403 Forbidden
> - SSL trust store setup (`cacerts-nrm`)
> - Architecture rules: split-brain `@PreAuthorize`, role mapping via `app-code`

---

## JBoss EAP 8 Checklist

- `<packaging>war</packaging>` in pom.xml
- `ServletInitializer extends SpringBootServletInitializer`
- JDBC driver `<scope>provided</scope>`
- `WEB-INF/jboss-deployment-structure.xml` to exclude conflicting JBoss modules
- `maven-compiler-plugin` with `<parameters>true</parameters>`
- Virtual threads: `spring.threads.virtual.enabled: true`

## Testing Strategy

- Unit tests: mock repositories, test service logic
- Integration tests: use `@SpringBootTest` with embedded H2 (not mocking the DB) 
- `@WithMockUser(roles = "ADMIN")` for security tests
- Verify soft-delete: deleted rows must not appear in query results