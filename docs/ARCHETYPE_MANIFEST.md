# Archetype Manifest — Complete File Checklist

This document lists all files that should exist in the archetype folder. Dino uses this to validate and customize template files.

---

## ✅ BACKEND FILES

### Configuration & Entry Points
```
backend/
├── pom.xml                          ✅ Maven configuration (created)
├── src/main/java/id/co/bankmandiri/ep/undanganku/
│   ├── Application.java             ✅ Spring Boot entry point (created)
│   ├── ServletInitializer.java      ✅ DONE
│   │
│   ├── config/
│   │   ├── SecurityConfig.java      ✅ DONE
│   │   ├── JwtConfig.java           ✅ DONE
│   │   ├── CorsConfig.java          ✅ DONE
│   │   └── AuditingConfig.java      ✅ DONE
│   │
│   ├── domain/
│   │   ├── BaseEntity.java          ✅ DONE
│   │   ├── User.java                ✅ DONE
│   │   └── Role.java                ✅ DONE
│   │
│   ├── repository/
│   │   ├── UserRepository.java      ✅ DONE
│   │   └── RoleRepository.java      ✅ DONE
│   │
│   ├── service/
│   │   ├── AuthService.java         ✅ DONE
│   │   └── UserService.java         ✅ DONE
│   │
│   ├── controller/
│   │   ├── AuthController.java      ✅ DONE
│   │   ├── UserController.java      ✅ DONE
│   │   └── HealthController.java    ✅ DONE
│   │
│   ├── dto/
│   │   ├── LoginRequest.java        ✅ DONE
│   │   ├── LoginResponse.java       ✅ DONE
│   │   ├── UserResponse.java        ✅ DONE
│   │   └── UserCreateRequest.java   ✅ DONE
│   │
│   ├── security/
│   │   ├── JwtAuthenticationFilter.java ✅ DONE
│   │   ├── JwtUtil.java              ✅ DONE
│   │   ├── PasswordUtil.java         ✅ DONE
│   │   └── CustomUserDetailsService.java ✅ DONE
│   │
│   └── exception/
│       ├── GlobalExceptionHandler.java ✅ DONE
│       ├── ResourceNotFoundException.java ✅ DONE
│       └── AuthenticationException.java  ✅ DONE
│
└── src/main/resources/
    ├── application.yml              ✅ DONE
    ├── application-local.yml        ✅ DONE
    ├── logback-spring.xml           ✅ DONE
    └── db/migration/
        ├── oracle/
        │   ├── V1__initial_schema.sql     ✅ DONE
        │   └── V2__initial_data.sql       ✅ DONE
        └── postgresql/
            ├── V1__initial_schema.sql     ✅ DONE
            └── V2__initial_data.sql       ✅ DONE
```

---

## ✅ FRONTEND FILES

### Core Configuration
```
frontend/
├── package.json                     ✅ DONE
├── angular.json                     ✅ DONE
├── tsconfig.json                    ✅ DONE
├── tsconfig.app.json                ✅ DONE
├── tsconfig.spec.json               ✅ DONE
├── tailwind.config.js               ✅ DONE
│
└── src/
    ├── main.ts                      ✅ DONE
    ├── index.html                   ✅ DONE
    ├── styles.css                   ✅ DONE
    │
    └── app/
        ├── app.component.ts         ✅ DONE
        ├── app.config.ts            ✅ DONE
        ├── app.routes.ts            ✅ DONE
        │
        ├── core/
        │   ├── models/
        │   │   ├── user.model.ts        ✅ DONE
        │   │   └── auth.model.ts        ✅ DONE
        │   │
        │   ├── auth.service.ts          ✅ DONE
        │   ├── auth.guard.ts            ✅ DONE
        │   └── auth.interceptor.ts      ✅ DONE
        │
        ├── layout/
        │   ├── app-layout.component.ts  ✅ DONE
        │   └── app-layout.component.html ✅ DONE
        │
        └── features/
            ├── auth/
            │   ├── login.component.ts    ✅ DONE
            │   └── login.component.html  ✅ DONE
            │
            ├── dashboard/
            │   ├── admin-dashboard.component.ts   ✅ DONE
            │   ├── admin-dashboard.component.html ✅ DONE
            │   ├── user-dashboard.component.ts    ✅ DONE
            │   └── user-dashboard.component.html  ✅ DONE
            │
            └── users/
                ├── user-list.component.ts  ✅ DONE
                └── user-list.component.html ✅ DONE
```

---

## ✅ DOCUMENTATION FILES

```
docs/
├── index.html                       ⚠️ Documentation site (from template)
├── ARCHITECTURE.md                  ⚠️ Tech stack decisions
├── SETUP.md                         ⚠️ Step-by-step setup
├── API.md                           ⚠️ API endpoints reference
├── AUTHENTICATION.md                ⚠️ JWT flow explanation
├── DATABASE.md                      ⚠️ Schema & migrations
├── FRONTEND.md                      ⚠️ Component structure
└── DEVELOPMENT.md                   ⚠️ Day-to-day workflow
```

---

## ✅ CONFIGURATION FILES

```
undanganku/
├── CLAUDE.md                        ⚠️ Development rules + agent refs
├── docker-compose.yml               ⚠️ PostgreSQL 16 local setup
├── .gitignore                       ⚠️ Java + Node.js patterns
├── .env.example                     ⚠️ Environment variables template
└── README.md                        ✅ Quick start guide
```

---

## 📊 FILE STATUS

- ✅ **Created:** 2 files (pom.xml, Application.java, README.md)
- ⚠️ **To Create:** 50+ files (skeleton templates)

---

## 🎯 STRATEGY: Creating Remaining Files

### Phase 1: Essential Backend (Priority)
```
1. Database migrations (V1, V2)      ← Most important for schema
2. User, Role entities
3. AuthService, AuthController
4. SecurityConfig, JwtUtil
5. application.yml, application-local.yml
```

### Phase 2: Frontend (Priority)
```
1. package.json, angular.json
2. AuthService, AuthGuard, Interceptor
3. LoginComponent, RegisterComponent
4. App routes
```

### Phase 3: Rest of Backend
```
1. Controllers, Services
2. DTOs, Exception handlers
3. Config classes
```

### Phase 4: Documentation
```
1. CLAUDE.md
2. Setup guide
3. Architecture doc
```

---

## 📝 FILE CONTENT TEMPLATES

For files marked `⚠️ TO CREATE`, use these templates as basis:

### Java Class Template
```java
package id.co.bankmandiri.ep.undanganku.domain;

public class {{ClassName}} extends BaseEntity {
    
    // Fields
    private String field1;
    private Integer field2;
    
    // Constructors (explicit, no Lombok!)
    public {{ClassName}}() {}
    
    public {{ClassName}}(String field1, Integer field2) {
        this.field1 = field1;
        this.field2 = field2;
    }
    
    // Getters/Setters
    public String getField1() { return field1; }
    public void setField1(String field1) { this.field1 = field1; }
    
    public Integer getField2() { return field2; }
    public void setField2(Integer field2) { this.field2 = field2; }
}
```

### Angular Component Template
```typescript
import { Component, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-component-name',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule],
  template: `<div>Component content</div>`,
  styles: []
})
export class ComponentNameComponent {
  // Component logic
}
```

### SQL Migration Template
```sql
-- V{n}__description.sql

CREATE TABLE table_name (
    id BIGSERIAL PRIMARY KEY,
    guid UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
    field1 VARCHAR(255) NOT NULL,
    field2 INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL,
    deleted_at TIMESTAMP NULL
);

CREATE INDEX idx_table_name_deleted_at ON table_name (deleted_at);
CREATE INDEX idx_table_name_created_at ON table_name (created_at);
```

---

## ✅ VERIFICATION CHECKLIST (Before Dino Use)

- [ ] All files paths correct
- [ ] Placeholders (undanganku, Undanganku, etc) consistent
- [ ] No hardcoded values (except defaults)
- [ ] Database migrations V1 + V2 complete
- [ ] Frontend components skeleton ready
- [ ] Configuration files template
- [ ] Documentation template
- [ ] .gitignore includes node_modules/, target/, dist/
- [ ] Package.json has correct dependencies
- [ ] pom.xml has correct Spring Boot version

---

## 🔄 HOW DINO USES THIS MANIFEST

1. **Load archetype manifest**
2. **For each file in manifest:**
   - Check if file exists
   - If exists: read template
   - If missing: use default template
   - Replace {{PLACEHOLDERS}} with project values
3. **Generate project directory**
4. **Create all files from customized templates**

---

## 📋 FILE GENERATION ORDER (Dino executes this order)

1. Copy .gitignore
2. Create base config (application.yml, docker-compose.yml, CLAUDE.md)
3. Create database migrations (V1, V2)
4. Create Java entities (User, Role, BaseEntity)
5. Create repositories, services, controllers
6. Create security classes (JwtUtil, AuthFilter, etc)
7. Create frontend structure
8. Create Angular services, components, routes
9. Create documentation
10. Done!

---

**Version:** 1.0.0  
**Last Updated:** 2026-05-03  
**Status:** Ready for Dino implementation

