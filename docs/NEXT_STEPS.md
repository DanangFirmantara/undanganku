# SETUP COMPLETE - Next Steps untuk Developer

Tanggal: 2026-05-03
Aplikasi: Undanganku
Status: Ready for Development

---

## 1. Verifikasi Environment

### Pastikan sudah installed:
- [ ] Java 21 (Temurin)
- [ ] Maven 3.9+
- [ ] Node.js 20 LTS
- [ ] Docker & Docker Compose
- [ ] Git

Cek dengan:
\\\ash
java -version
mvn -version
node -v
npm -v
docker --version
docker-compose --version
\\\

---

## 2. Database Connection Test

Aplikasi sudah dikonfigurasi untuk connect ke:
- Host: 103.93.129.174
- Port: 5432
- Database: undanganku
- Username: myuser
- Password: P@ssw0rd

Test connection:
\\\ash
psql -h 103.93.129.174 -U myuser -d undanganku
# Ketik password: P@ssw0rd
\\\

---

## 3. Backend Setup & Run

\\\ash
cd C:\Users\Administrator\OneDrive\Documents\All About Claude\Project Koding\Undanganku\backend

# Install dependencies
mvn clean install

# Set JWT Secret (Windows)
set JWT_SECRET=your-development-secret-key-min-32-chars

# Run Spring Boot
mvn spring-boot:run

# OR dengan specific profile
mvn -Dspring-boot.run.profiles=local spring-boot:run
\\\

Backend akan run di: http://localhost:8080
API Endpoint: http://localhost:8080/ipa-undanganku

### Endpoints untuk Testing:
- POST   /ipa-undanganku/api/auth/register
- POST   /ipa-undanganku/api/auth/login
- POST   /ipa-undanganku/api/auth/refresh
- GET    /ipa-undanganku/api/health

---

## 4. Frontend Setup & Run

\\\ash
cd C:\Users\Administrator\OneDrive\Documents\All About Claude\Project Koding\Undanganku\frontend

# Install dependencies
npm install

# Development server
npm start

# Build untuk production
npm run build:prod

# Run tests
npm test

# Lint code
npm run lint
\\\

Frontend akan run di: http://localhost:4200

---

## 5. Project Structure Overview

\\\
C:\Users\Administrator\OneDrive\Documents\All About Claude\Project Koding\Undanganku\
├── backend/
│   ├── src/main/java/id/co/bankmandiri/ep/undanganku/
│   │   ├── config/          → Spring configurations
│   │   ├── domain/          → JPA entities
│   │   ├── repository/      → Data access layer
│   │   ├── service/         → Business logic
│   │   ├── controller/      → REST endpoints
│   │   ├── dto/             → Data Transfer Objects
│   │   ├── security/        → JWT & authentication
│   │   └── exception/       → Custom exceptions
│   ├── src/main/resources/
│   │   ├── application.yml  → Main config
│   │   └── db/migration/    → Flyway migrations
│   └── pom.xml
│
├── frontend/
│   ├── src/app/
│   │   ├── core/            → Core services, guards
│   │   ├── layout/          → Layout components
│   │   ├── shared/          → Shared components, pipes, directives
│   │   └── features/        → Feature modules (auth, dashboard, builder)
│   ├── package.json
│   ├── angular.json
│   ├── tsconfig.json
│   └── tailwind.config.js
│
├── docker-compose.yml       → PostgreSQL local development
├── CLAUDE.md               → Development rules & agent references
├── README.md               → Quick start guide
└── ARCHETYPE_MANIFEST.md   → Template documentation
\\\

---

## 6. Key Configuration Files

### Backend: application.yml
Location: \ackend/src/main/resources/application.yml\

Currently configured with:
- PostgreSQL: 103.93.129.174:5432/undanganku
- Spring Boot: 3.3.0 (Java 21)
- JWT: Ekspiration 15 menit
- Flyway: Enabled untuk DB migrations
- Hibernate: DDL validation

### Frontend: angular.json
Location: \rontend/angular.json\

Configured untuk:
- Angular 18
- Tailwind CSS
- Development & Production builds

---

## 7. Development Workflow

### Git Setup (if not already done)
\\\ash
cd C:\Users\Administrator\OneDrive\Documents\All About Claude\Project Koding\Undanganku
git init
git add .
git commit -m "Initial commit: Undanganku project setup"
\\\

### Branching Strategy (recommended)
\\\ash
# Create feature branch
git checkout -b feature/feature-name

# Work, commit regularly
git add .
git commit -m "Descriptive message"

# Push & create PR
git push -u origin feature/feature-name
\\\

---

## 8. Default Credentials (untuk testing)

Admin User:
- Email: admin@app.com
- Password: Admin@123456
- Role: ROLE_ADMIN

Test User:
- Email: user@app.com
- Password: User@123456
- Role: ROLE_USER

** CHANGE THESE IMMEDIATELY IN PRODUCTION!

---

## 9. Port Reference

- Backend: 8080 (Spring Boot)
- Frontend: 4200 (Angular dev server)
- PostgreSQL: 5432 (external server)

Jika ada port conflict, edit:
- Backend: \pplication.yml\ - section \server.port\
- Frontend: \ngular.json\ - section \serve.options.port\

---

## 10. Common Tasks

### Database Migration
\\\ash
# Migrations run automatically saat app start
# Manual migrations di: backend/src/main/resources/db/migration/postgresql/

# Check Flyway status
mvn flyway:info
\\\

### Build & Package untuk Production
\\\ash
# Backend
cd backend
mvn clean package

# Frontend
cd frontend
npm run build:prod
\\\

### Run Tests
\\\ash
# Backend
cd backend
mvn test

# Frontend
cd frontend
npm test
\\\

---

## 11. Important Files to Review

Sebelum mulai coding, baca:

1. **CLAUDE.md** - Development rules & agent references
2. **docs/SETUP.md** - Detailed setup instructions
3. **docs/ARCHITECTURE.md** - Tech stack decisions
4. **docs/API_SPECIFICATION.md** - REST API design
5. **docs/DATABASE.md** - Database schema & migrations

---

## 12. Troubleshooting

### Error: Connection refused (database)
\\\
Pastikan:
- Database server 103.93.129.174 accessible
- Network/firewall allows port 5432
- Credentials benar: myuser / P@ssw0rd
\\\

### Error: Port already in use
\\\
Windows:
netstat -ano | findstr :8080
taskkill /PID <PID> /F

Linux/Mac:
lsof -i :8080
kill -9 <PID>
\\\

### Frontend: Blank page / API errors
\\\
- Clear browser cache (Ctrl+Shift+Delete)
- Check backend running: curl http://localhost:8080/ipa-undanganku/api/health
- Check console for CORS errors
- Ensure proxy.conf.json correct (jika ada)
\\\

### Java/Maven issues
\\\
mvn clean install -U    # Update dependencies
mvn dependency:tree     # Check dependency tree
\\\

---

## 13. Next Development Phase

1. [ ] Setup local environment & verify all connections
2. [ ] Review CLAUDE.md & architecture documentation
3. [ ] Understand database schema (docs/DATABASE.md)
4. [ ] Review existing code structure
5. [ ] Test default credentials on login
6. [ ] Create first feature branch & make test commit
7. [ ] Integration test: frontend ↔ backend API

---

## Support & Questions

- Framework docs: https://angular.io & https://spring.io
- JWT Implementation: Check \ackend/src/main/java/.../security/\
- Database: PostgreSQL 16 (check \docker-compose.yml\)

---

Generated: 2026-05-03
Dino V2 Setup Agent
