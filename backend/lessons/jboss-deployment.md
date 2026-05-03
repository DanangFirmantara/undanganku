# Lesson: JBoss EAP 8 Deployment — Undanganku

## Packaging Checklist
- `<packaging>war</packaging>` di pom.xml
- `ServletInitializer extends SpringBootServletInitializer`
- PostgreSQL driver: `<scope>runtime</scope>` untuk local, `<scope>provided</scope>` untuk production JBoss
- `maven-compiler-plugin` dengan `<parameters>true</parameters>`
- `WEB-INF/jboss-deployment-structure.xml` — exclude modul JBoss yang konflik dengan Spring

## Context Path
- Production: `/ipa-undanganku`
- Semua API endpoint: `POST /ipa-undanganku/api/auth/login`, dst.

## Local Dev (Spring Boot run — tanpa JBoss)
```bash
cd backend
mvn clean install
mvn -Dspring-boot.run.profiles=local spring-boot:run
```
- Port: 8080
- Context path: `/ipa-undanganku`

## Build WAR untuk Deploy
```bash
cd backend
mvn clean package -DskipTests
# Output: target/ipa-undanganku-backend-1.0.0.war
```

## Frontend Build + Bundle ke WAR
```bash
cd frontend && npm run build
cd ../backend && mvn clean package -DskipTests
```
Angular output di `frontend/dist` → dikopi ke `backend/src/main/resources/static` via `maven-resources-plugin`.

## Virtual Threads
```yaml
spring:
  threads:
    virtual:
      enabled: true
```
Java 21 virtual threads aktif — cocok untuk JBoss EAP 8 + Spring Boot 3.3.

## Application.java — Aturan @EnableJpaAuditing

`@EnableJpaAuditing` hanya boleh ada di **satu tempat**. Project ini menaruhnya di `AuditingConfig.java`.

```java
// BENAR — Application.java bersih
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// SALAH — duplikat dengan AuditingConfig → BeanDefinitionOverrideException
@SpringBootApplication
@EnableJpaAuditing   // ← JANGAN, sudah ada di AuditingConfig
public class Application { ... }
```

Error yang muncul jika duplikat:
```
BeanDefinitionOverrideException: Invalid bean definition with name 'jpaAuditingHandler'
```

## Troubleshooting

| Gejala | Penyebab | Fix |
|---|---|---|
| Port 8080 already in use | Proses lain pakai port | Kill via PowerShell: `Stop-Process -Id (Get-NetTCPConnection -LocalPort 8080 -State Listen).OwningProcess -Force` |
| `BeanDefinitionOverrideException: jpaAuditingHandler` | `@EnableJpaAuditing` duplikat | Hapus dari `Application.java`, biarkan hanya di `AuditingConfig.java` |
| `Unable to find a suitable main class` | `Application.java` belum dibuat | Buat file entry point dengan `@SpringBootApplication` |
| WAR deploy gagal di JBoss | Module conflict | Tambahkan exclusion di `jboss-deployment-structure.xml` |
| Frontend blank page | Backend tidak jalan / path salah | Cek `baseHref` di `angular.json`, pastikan backend up |
