-- V2__initial_data.sql
-- Insert default roles and test users

-- Insert default roles
INSERT INTO roles (guid, name, description, created_by, updated_by) VALUES
  (gen_random_uuid(), 'ROLE_ADMIN', 'Administrator with full access', 'system', 'system'),
  (gen_random_uuid(), 'ROLE_USER', 'Regular user with limited access', 'system', 'system');

-- Insert admin user
-- Email: admin@app.com
-- Password: Admin@123456 (BCrypt hash)
INSERT INTO users (guid, email, password_hash, first_name, last_name, is_active, created_by, updated_by) VALUES
  (gen_random_uuid(),
   'admin@app.com',
   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36DxwuKe',
   'Admin',
   'User',
   true,
   'system',
   'system');

-- Insert test user
-- Email: user@app.com
-- Password: User@123456 (BCrypt hash)
INSERT INTO users (guid, email, password_hash, first_name, last_name, is_active, created_by, updated_by) VALUES
  (gen_random_uuid(),
   'user@app.com',
   '$2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUm',
   'Test',
   'User',
   true,
   'system',
   'system');

-- Assign ROLE_ADMIN to admin user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.email = 'admin@app.com' AND r.name = 'ROLE_ADMIN';

-- Assign ROLE_USER to test user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.email = 'user@app.com' AND r.name = 'ROLE_USER';
