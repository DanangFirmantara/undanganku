# Deployment Report — Undanganku
**Tanggal:** 2026-05-03  
**Server:** 103.93.129.174 (Ubuntu 22.04 LTS)  
**Dilakukan oleh:** Claude Code

---

## Kondisi Server Sebelum Deploy

| Komponen | Status | Versi |
|---|---|---|
| OS | ✅ Aktif | Ubuntu 22.04.5 LTS |
| Docker | ✅ Terinstall | 29.4.0 |
| Nginx | ✅ Terinstall | 1.18.0 |
| Java | ❌ Tidak ada | — |
| PostgreSQL (sistem) | ⚠️ Inactive | 14 (tidak dipakai) |
| PostgreSQL (Docker) | ✅ Running | 16-alpine (pos_postgres) |
| CPU | 2 core | — |
| RAM | 1.9 GB (860MB used) | — |
| Disk | 58 GB (11GB used) | — |

**Aplikasi yang sudah berjalan (pos-app):**
- `pos_postgres` — PostgreSQL 16 di port 5432
- `pos_backend` — port 8080
- `pos_frontend` — port 3000  
- `pos_pgadmin` — port 5050

---

## Apa yang Ditambahkan

1. **Database `undanganku_db`** — dibuat di container `pos_postgres` yang sudah ada
   - User: `undanganku_user` / `Undanganku@2024`
   - Flyway otomatis menjalankan 3 migrasi saat pertama start

2. **Docker image backend** — `danang837/undanganku-backend:1.0`
   - Base image: WildFly 32.0.1.Final JDK 21 (`quay.io/wildfly/wildfly:32.0.1.Final-jdk21`)
   - WAR: `ipa-undanganku-backend-1.0.0.war` di-deploy ke WildFly
   - Fix: `jboss-deployment-structure.xml` untuk resolve konflik SLF4J/Logback

3. **Docker image frontend** — `danang837/undanganku-frontend:1.0`
   - Base image: nginx:alpine
   - Angular 18 static build di-serve via Nginx

4. **Docker Compose** — `/home/admin-dabul/undanganku/docker-compose.yml`
   - Backend container: `undanganku_backend` (port 8081)
   - Frontend container: `undanganku_frontend` (port 4000)
   - Terhubung ke network `pos-app_pos_network` yang sudah ada

5. **Nginx virtual host** — `/etc/nginx/sites-available/undanganku`
   - Port 80 sekarang melayani Undanganku
   - Default site Nginx dihapus dari sites-enabled

---

## Arsitektur Deployment

```
Internet
   │
   ▼
Nginx :80
   ├── /           → undanganku_frontend :4000 (Angular SPA)
   ├── /api/       → undanganku_backend :8081/ipa-undanganku/api/
   └── /ipa-undanganku/ → undanganku_backend :8081/ipa-undanganku/

undanganku_backend → pos_postgres:5432/undanganku_db
```

---

## URL Akses

| URL | Keterangan |
|---|---|
| `http://103.93.129.174/` | Frontend (Angular) |
| `http://103.93.129.174/ipa-undanganku/` | Backend WildFly (langsung) |
| `http://103.93.129.174/api/auth/login` | Login API |
| `http://103.93.129.174:4000/` | Frontend (direct port) |
| `http://103.93.129.174:8081/ipa-undanganku/` | Backend (direct port) |
| `http://103.93.129.174:5050/` | pgAdmin (pos-app) |

---

## Kredensial Default

| Akun | Email | Password | Role |
|---|---|---|---|
| Admin | admin@app.com | Admin@123456 | ROLE_ADMIN |
| User | user@app.com | User@123456 | ROLE_USER |

**JWT Secret:** `48a4e4f63804be637dc751909c403beea0685761987bb665521b3339eb395094`  
> Simpan secret ini di tempat aman! Jangan expose ke publik.

---

## File yang Dibuat/Diubah (Lokal)

| File | Perubahan |
|---|---|
| `backend/Dockerfile` | Update dari `java -jar` ke WildFly 32 JDK21 |
| `backend/src/main/webapp/WEB-INF/jboss-deployment-structure.xml` | Baru — fix konflik SLF4J WildFly vs Spring Boot |

---

## File di Server

| Path | Keterangan |
|---|---|
| `/home/admin-dabul/undanganku/docker-compose.yml` | Docker Compose undanganku |
| `/etc/nginx/sites-available/undanganku` | Nginx virtual host |
| `/etc/nginx/sites-enabled/undanganku` | Symlink aktif |

---

## Catatan

- **WildFly 32** dipilih sebagai pengganti JBoss EAP 8 (upstream gratis, fitur setara)
- Konflik logging antara WildFly dan Spring Boot diselesaikan dengan `jboss-deployment-structure.xml`
- Container backend perlu ~45 detik untuk startup penuh (WildFly lebih berat dari embedded Tomcat)
- SSL/HTTPS belum dikonfigurasi — disarankan setup Certbot + Let's Encrypt untuk production
- Port 3000 (pos_frontend) masih bisa diakses langsung, tidak terganggu

---

## Perintah Berguna di Server

```bash
# Cek status container
docker ps --filter name=undanganku

# Lihat log backend
docker logs undanganku_backend --tail 50 -f

# Restart
cd ~/undanganku && docker compose restart

# Update image terbaru
cd ~/undanganku && docker compose pull && docker compose up -d
```
