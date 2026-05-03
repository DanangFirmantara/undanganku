# Lesson: Auth & JWT — Undanganku Frontend

## Token Storage
```
localStorage:
  auth_token    → JWT string
  auth_user     → JSON serialized User object
  auth_expires  → timestamp (ms) kapan token expired
```

## AuthService (pattern aktual)
- `authState` adalah `signal<AuthState>` readonly
- `isLoggedIn` = `computed(() => authState().isLoggedIn)`
- `currentUser` = `computed(() => authState().user)`
- `login()` → POST `/api/auth/login` → `setAuthState()` → navigate `/dashboard`
- `logout()` → POST `/api/auth/logout` → `clearAuthState()` → navigate `/login`
- Auto-logout via `setTimeout` sesuai `expiresAt` — di-trigger oleh `effect()`

## Auth Interceptor
```typescript
// Attach Bearer token ke semua request kecuali /api/auth/login
Authorization: Bearer <token>
```
File: `frontend/src/app/core/auth.interceptor.ts`

## Auth Guard
- Proteksi semua route kecuali `/login`
- Cek `auth.isLoggedIn()` → jika false, navigate ke `/login`
- File: `frontend/src/app/core/auth.guard.ts` (perlu dibuat jika belum ada)

## Role Guard
- Cek `auth.authState().user?.roles?.includes(requiredRole)`
- Jika gagal, redirect ke `/dashboard`

## Auto-Logout Behavior
- Token expired (15 menit) → `setTimeout` fire → `logout()` → navigate `/login`
- Jika user sudah tutup browser → saat buka lagi, `hasValidToken()` cek `expiresAt` vs `Date.now()`
- Jika expired → `isLoggedIn = false` → auth guard redirect ke `/login`

## LoginResponse Interface
```typescript
interface LoginResponse {
  token: string;
  expiresIn: number;   // ms — 900000 (15 menit)
  user: User;
}
```

## Troubleshooting

| Gejala | Penyebab | Fix |
|---|---|---|
| Blank page setelah login | `authState` signal tidak update | Pastikan `setAuthState()` dipanggil setelah response |
| Auto-logout terlalu cepat | `expiresIn` dalam detik bukan ms | Pastikan backend return ms, bukan detik |
| 401 pada semua request | Token tidak diattach | Cek interceptor terdaftar di `app.config.ts` |
| Loop redirect login | Auth guard check kondisi race | Pastikan `hasValidToken()` sync, tidak async |
