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

## Troubleshooting

| Gejala | Penyebab | Fix |
|---|---|---|
| Port 8080 already in use | Proses lain pakai port | Ubah port di `application-local.yml` atau kill prosesnya |
| WAR deploy gagal di JBoss | Module conflict | Tambahkan exclusion di `jboss-deployment-structure.xml` |
| Frontend blank page | Backend tidak jalan / path salah | Cek `baseHref` di `angular.json`, pastikan backend up |
