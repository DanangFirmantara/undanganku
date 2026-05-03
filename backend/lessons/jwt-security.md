# Lesson: JWT Security — Undanganku

## Stack
- Library: JJWT 0.12.3
- Algorithm: HS256
- Expiry: 900000 ms (15 menit)
- Secret: env var `JWT_SECRET`

## Auth Flow
1. `POST /api/auth/login` → validate credentials → generate JWT → return `{token, expiresIn, user}`
2. Frontend simpan token di `localStorage` (key: `auth_token`)
3. Setiap request: `Authorization: Bearer <token>`
4. `JwtAuthFilter` (extends `OncePerRequestFilter`) validate token → set `SecurityContextHolder`
5. Token expired → 401 → frontend auto-logout → redirect `/login`

## SecurityConfig Permit Rules
```
/api/auth/**       → permitAll
/actuator/health   → permitAll
/api/admin/**      → hasRole("ADMIN")
/** (static)       → permitAll
anyRequest         → authenticated
```

## Role Convention
- Roles disimpan di tabel `roles`, relasi many-to-many via `user_roles`
- Spring Security role: `ROLE_ADMIN`, `ROLE_USER`
- Di `@PreAuthorize`: gunakan `hasRole('ADMIN')` — Spring menambahkan prefix `ROLE_` otomatis
- Di JWT claims: simpan sebagai list `["ROLE_ADMIN"]`

## Password Policy
- Minimal 8 karakter, alphanumeric
- Hashing: BCrypt (`BCryptPasswordEncoder`)
- Kolom di DB: `password_hash VARCHAR(255)`

## Troubleshooting

| Gejala | Kemungkinan Penyebab | Fix |
|---|---|---|
| 401 pada semua request | JWT_SECRET tidak di-set | Set env var JWT_SECRET |
| Token valid tapi 403 | User tidak punya role yang diminta | Cek tabel user_roles |
| Auto-logout terlalu cepat | Clock skew server vs client | Verifikasi timezone server |
| `SignatureException` | JWT_SECRET berbeda antara generate dan validate | Pastikan satu secret konsisten |
