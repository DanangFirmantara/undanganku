# Lesson: Database & Flyway — Undanganku

## Stack
- Database: PostgreSQL 16
- Migration tool: Flyway (SQL only, bukan Liquibase)
- Location: `classpath:db/migration/postgresql`
- Oracle folder ada tapi **tidak digunakan** — jangan tambahkan file baru di sana

## Audit Columns (7 field — wajib setiap tabel)
```sql
id          BIGSERIAL    PRIMARY KEY,
guid        UUID         NOT NULL UNIQUE DEFAULT gen_random_uuid(),
...business columns...
created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
created_by  VARCHAR(100) NOT NULL,
updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_by  VARCHAR(100) NOT NULL,
deleted_at  TIMESTAMP    NULL
```

## Soft-Delete Pattern
- `deleted_at IS NULL` = aktif
- `deleted_at IS NOT NULL` = soft-deleted
- Di entity: `@SQLDelete` + `@SQLRestriction("deleted_at IS NULL")`
- Index wajib: `CREATE INDEX idx_{table}_deleted_at ON {table} (deleted_at);`
- Unique constraint aktif saja: `CREATE UNIQUE INDEX uq_{table}_{col} ON {table} ({col}) WHERE deleted_at IS NULL;`

## Existing Migrations

| File | Isi |
|---|---|
| `V1__initial_schema.sql` | `roles`, `users`, `user_roles`, `audit_logs` |
| `V2__initial_data.sql` | Seed roles, admin user, test user |

## Aturan Flyway
- **Jangan pernah edit file migration yang sudah di-commit** — Flyway akan gagal karena checksum berubah
- Nomor versi sequential: V3, V4, dst.
- Satu file per versi (bukan per vendor)
- Deskripsi nama file: `V3__add_invitation_table.sql`

## application.yml Flyway Config
```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration/postgresql
    out-of-order: false
```

## Troubleshooting

| Gejala | Penyebab | Fix |
|---|---|---|
| `FlywayException: checksum mismatch` | File migration yang sudah di-run diedit | Kembalikan file ke kondisi original atau repair via `flyway repair` |
| Migration tidak jalan | Versi tidak sequential / ada gap | Cek file existing, pastikan nomor lanjut |
| `PSQLException: relation already exists` | Migration dijalankan dua kali tanpa checksum | Periksa tabel `flyway_schema_history` |
