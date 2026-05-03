# Lesson: Local Dev Setup — Undanganku Frontend

## Dev Server
- Angular dev server: `http://localhost:4200`
- App URL: `http://localhost:4200/ipa-undanganku` (sesuai baseHref di angular.json)
- Backend: `http://localhost:8080/ipa-undanganku`

## Proxy Configuration

File: `frontend/proxy.conf.json` — sudah didaftarkan di `angular.json` serve options.

```json
{
  "/api": {
    "target": "http://localhost:8080/ipa-undanganku",
    "secure": false,
    "changeOrigin": true,
    "logLevel": "info"
  }
}
```

Request flow saat dev:
```
Browser → POST http://localhost:4200/api/auth/login
                ↓ Angular proxy
          POST http://localhost:8080/ipa-undanganku/api/auth/login
```

**Penting:** Jika backend port berubah, update `target` di `proxy.conf.json`. Restart Angular dev server setelah mengubah proxy config agar perubahan dimuat.

## Menjalankan Dev Environment

```bash
# Terminal 1 — Backend
cd backend
mvn spring-boot:run

# Terminal 2 — Frontend
cd frontend
npm start
# Akses: http://localhost:4200/ipa-undanganku
```

## angular.json — Konfigurasi Penting

```json
"build": {
  "options": {
    "baseHref": "/ipa-undanganku/"   ← path prefix untuk semua asset dan route
  }
},
"serve": {
  "options": {
    "port": 4200,
    "proxyConfig": "proxy.conf.json"  ← proxy sudah terdaftar
  }
}
```

## Troubleshooting

| Gejala | Penyebab | Fix |
|---|---|---|
| Login 404 via frontend | proxy.conf.json target port salah | Pastikan target mengarah ke port backend yang benar (8080) |
| Proxy tidak efek setelah diubah | Angular masih pakai config lama | Restart Angular dev server |
| Port 4200 already in use | Dev server masih jalan | `Stop-Process -Id (Get-NetTCPConnection -LocalPort 4200 -State Listen).OwningProcess -Force` |
| Blank page di `/` | baseHref `/ipa-undanganku/` → akses via path yang benar | Buka `http://localhost:4200/ipa-undanganku` bukan `http://localhost:4200` |
