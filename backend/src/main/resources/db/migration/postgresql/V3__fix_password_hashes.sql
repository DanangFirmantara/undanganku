-- V3__fix_password_hashes.sql
-- Fix BCrypt password hashes for seeded users
-- Admin@123456 and User@123456

UPDATE users SET password_hash = '$2a$10$1db8y6c3WTs95B48oYEo9.kVCsk7OyFG2KRau0FdrqUKxexxp6Awi'
WHERE email = 'admin@app.com';

UPDATE users SET password_hash = '$2a$10$tjLYC10tsj7rwCxdTolriOMTBo.E7uRGfSZMbDwFQ5BtuSC4EIaLu'
WHERE email = 'user@app.com';
