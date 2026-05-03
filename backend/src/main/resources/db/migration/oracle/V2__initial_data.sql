-- V2__initial_data.sql - Oracle version
-- Insert roles
INSERT INTO roles (guid, name, description, created_by, updated_by)
VALUES (SYS_GUID(), 'ROLE_ADMIN', 'Administrator role', 'SYSTEM', 'SYSTEM');

INSERT INTO roles (guid, name, description, created_by, updated_by)
VALUES (SYS_GUID(), 'ROLE_USER', 'Regular user role', 'SYSTEM', 'SYSTEM');

-- Insert admin user (password: Admin@123456)
INSERT INTO users (guid, email, password_hash, first_name, last_name, is_active, created_by, updated_by)
VALUES (SYS_GUID(), 'admin@app.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36DxwuKe', 'Admin', 'User', 1, 'SYSTEM', 'SYSTEM');

-- Insert test user (password: User@123456)
INSERT INTO users (guid, email, password_hash, first_name, last_name, is_active, created_by, updated_by)
VALUES (SYS_GUID(), 'user@app.com', '$2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUm', 'Test', 'User', 1, 'SYSTEM', 'SYSTEM');

-- Assign admin role to admin user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.email = 'admin@app.com' AND r.name = 'ROLE_ADMIN';

-- Assign user role to admin user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.email = 'admin@app.com' AND r.name = 'ROLE_USER';

-- Assign user role to test user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.email = 'user@app.com' AND r.name = 'ROLE_USER';

COMMIT;
