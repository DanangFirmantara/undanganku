# Undanganku - Documentation Index

**Project Setup Date:** 2026-05-03  
**Status:** Ready for Development  
**Version:** 1.0.0

---

## Quick Navigation

### For Quick Start (5-10 minutes)
1. Read **NEXT_STEPS.md** - Section 1-3 (Environment Setup)
2. Run the Quick Start commands (Section 7)
3. Test login with admin credentials

### For Understanding the Project
1. **README.md** - Project overview
2. **CLAUDE.md** - Development rules & agent references
3. **docs/ARCHITECTURE.md** - Technology decisions
4. **docs/API_SPECIFICATION.md** - API design

### For Detailed Reference
- **SETUP_SUMMARY.txt** - Configuration reference (copy-paste friendly)
- **NEXT_STEPS.md** - Comprehensive guide with troubleshooting
- **docs/DATABASE.md** - Schema & migrations

---

## File Guide

### Root Level Documentation

| File | Purpose | Read Time |
|------|---------|-----------|
| **README.md** | Project overview, quick start, folder structure | 5 min |
| **NEXT_STEPS.md** | Detailed setup guide, development workflow, troubleshooting | 15 min |
| **SETUP_SUMMARY.txt** | Configuration reference, quick lookup guide | 5 min |
| **CLAUDE.md** | Development rules, agent references, workflow | 10 min |
| **ARCHETYPE_MANIFEST.md** | Archetype documentation, template structure | 10 min |
| **DOCUMENTATION_INDEX.md** | This file - navigation guide | 2 min |
| **docker-compose.yml** | PostgreSQL local development setup | Reference |
| **.gitignore** | Git ignore patterns for Java & Node.js | Reference |

### Backend Documentation (docs/ folder)

| File | Purpose | Audience |
|------|---------|----------|
| **docs/SETUP.md** | Detailed backend environment setup | Backend devs |
| **docs/ARCHITECTURE.md** | Tech stack decisions, why Spring Boot, why Angular | Team leads |
| **docs/DATABASE.md** | Schema design, migrations, Flyway setup | Backend devs, DBAs |
| **docs/AUTHENTICATION.md** | JWT implementation, auth flow, security | Backend devs |
| **docs/API_SPECIFICATION.md** | REST API design, endpoints, response format | Frontend & Backend |
| **docs/DEPLOYMENT.md** | Production deployment guide | DevOps/Deployment |

### Code-Level Documentation

| Location | Type | Purpose |
|----------|------|---------|
| `backend/src/main/java/.../` | JavaDoc | Class & method documentation |
| `frontend/src/app/` | JSDoc/TSDoc | Component documentation |
| `backend/pom.xml` | Comments | Maven dependencies, plugins |
| `frontend/tsconfig.json` | Comments | TypeScript configuration notes |

---

## Development Reference

### Quick Commands

**Backend:**
```bash
cd backend
set JWT_SECRET=your-secret-key
mvn spring-boot:run
```

**Frontend:**
```bash
cd frontend
npm install
npm start
```

**Database:**
```bash
psql -h 103.93.129.174 -U myuser -d undanganku
Password: P@ssw0rd
```

### Key URLs

| Service | URL | Notes |
|---------|-----|-------|
| Frontend App | http://localhost:4200 | Angular dev server |
| Backend API | http://localhost:8080/ipa-undanganku | Spring Boot |
| API Health | http://localhost:8080/ipa-undanganku/api/health | Status check |
| Database | 103.93.129.174:5432/undanganku | External PostgreSQL |

### Default Credentials

**Admin User (for testing):**
- Email: admin@app.com
- Password: Admin@123456
- Role: ROLE_ADMIN

**Test User:**
- Email: user@app.com
- Password: User@123456
- Role: ROLE_USER

---

## Project Structure Map

```
Undanganku/
│
├── 📖 DOCUMENTATION
│   ├── README.md                    ← Start here
│   ├── NEXT_STEPS.md               ← Setup guide
│   ├── SETUP_SUMMARY.txt           ← Quick reference
│   ├── CLAUDE.md                   ← Development rules
│   ├── DOCUMENTATION_INDEX.md       ← This file
│   └── docs/
│       ├── SETUP.md
│       ├── ARCHITECTURE.md
│       ├── DATABASE.md
│       ├── AUTHENTICATION.md
│       ├── API_SPECIFICATION.md
│       └── DEPLOYMENT.md
│
├── 🔧 BACKEND
│   ├── pom.xml                     ← Maven config
│   └── src/main/java/id/co/bankmandiri/ep/undanganku/
│       ├── Application.java        ← Entry point
│       ├── config/                 ← Spring configs
│       ├── domain/                 ← JPA entities
│       ├── repository/             ← Data access
│       ├── service/                ← Business logic
│       ├── controller/             ← REST endpoints
│       ├── dto/                    ← Data objects
│       ├── security/               ← JWT, Auth
│       └── exception/              ← Error handling
│   └── src/main/resources/
│       ├── application.yml         ← Main config
│       ├── application-local.yml   ← Dev config
│       └── db/migration/postgresql/← DB migrations
│
├── 🎨 FRONTEND
│   ├── package.json                ← npm config
│   ├── angular.json                ← Angular CLI config
│   ├── tsconfig.json               ← TypeScript config
│   ├── tailwind.config.js          ← Tailwind CSS
│   └── src/app/
│       ├── core/                   ← Services, guards
│       ├── layout/                 ← Layout components
│       ├── shared/                 ← Shared components
│       └── features/               ← Feature modules
│
├── 🗄️  DATABASE
│   ├── docker-compose.yml          ← Local PostgreSQL
│   └── External: 103.93.129.174:5432/undanganku
│
└── ⚙️  CONFIG
    ├── .gitignore
    ├── .claude/
    └── (other config)
```

---

## Reading Roadmap by Role

### 👨‍💻 Full Stack Developer (First Time)

**Week 1:**
1. README.md (5 min)
2. NEXT_STEPS.md sections 1-5 (20 min)
3. docs/ARCHITECTURE.md (10 min)
4. CLAUDE.md (10 min)

**Week 1-2:**
5. docs/DATABASE.md (10 min)
6. docs/API_SPECIFICATION.md (15 min)
7. docs/AUTHENTICATION.md (10 min)

**Before First Commit:**
8. Review code in backend/src/main/java/... (30 min)
9. Review code in frontend/src/app/ (30 min)

### 👨‍💼 Backend Developer

**Priority:**
1. NEXT_STEPS.md (backend section)
2. docs/DATABASE.md
3. docs/AUTHENTICATION.md
4. docs/API_SPECIFICATION.md
5. CLAUDE.md (backend guidelines)

### 🎨 Frontend Developer

**Priority:**
1. NEXT_STEPS.md (frontend section)
2. docs/API_SPECIFICATION.md
3. docs/ARCHITECTURE.md (frontend part)
4. CLAUDE.md (frontend guidelines)

### 🔧 DevOps/Deployment

**Priority:**
1. docs/DEPLOYMENT.md
2. SETUP_SUMMARY.txt (configuration reference)
3. docker-compose.yml
4. docs/ARCHITECTURE.md (infrastructure part)

---

## FAQ - Quick Answers

**Q: How do I run the application?**
A: See NEXT_STEPS.md sections 3-4, or run the Quick Start commands above.

**Q: What's the database connection info?**
A: Host: 103.93.129.174, Port: 5432, User: myuser, Password: P@ssw0rd

**Q: What are the default login credentials?**
A: Admin: admin@app.com / Admin@123456 (Change in production!)

**Q: Where's the API documentation?**
A: See docs/API_SPECIFICATION.md for REST endpoint design.

**Q: How do I set up JWT authentication?**
A: Set JWT_SECRET environment variable. See docs/AUTHENTICATION.md for details.

**Q: What if port 8080 is already in use?**
A: Edit backend/src/main/resources/application.yml, change server.port to 8081.

**Q: How do I run database migrations?**
A: Automatic on startup. Manual: mvn flyway:info

**Q: Where are the database migration files?**
A: backend/src/main/resources/db/migration/postgresql/

---

## Last Updated

**Timestamp:** 2026-05-03  
**By:** Dino V2 Setup Agent  
**Version:** 1.0.0

---

## Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | 2026-05-03 | Initial project setup |

---

## Support Contacts

For questions about:
- **Setup issues:** See NEXT_STEPS.md troubleshooting section
- **API design:** Review docs/API_SPECIFICATION.md
- **Database:** Check docs/DATABASE.md
- **Deployment:** Refer to docs/DEPLOYMENT.md
- **Development workflow:** Review CLAUDE.md

---

**Remember:** This documentation is generated once during project setup. Keep it updated as you add features and change configurations.
