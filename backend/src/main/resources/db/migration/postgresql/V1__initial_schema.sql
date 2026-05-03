-- V1__initial_schema.sql
-- PostgreSQL 16 - Users, Roles, and Audit tables

-- Roles table
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    guid UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL,
    deleted_at TIMESTAMP NULL
);

CREATE INDEX idx_roles_deleted_at ON roles (deleted_at);
CREATE INDEX idx_roles_created_at ON roles (created_at);

-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    guid UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NOT NULL,
    deleted_at TIMESTAMP NULL
);

CREATE UNIQUE INDEX uq_users_email ON users (email) WHERE deleted_at IS NULL;
CREATE INDEX idx_users_deleted_at ON users (deleted_at);
CREATE INDEX idx_users_created_at ON users (created_at);

-- User-Role many-to-many relationship
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE INDEX idx_user_roles_user_id ON user_roles (user_id);
CREATE INDEX idx_user_roles_role_id ON user_roles (role_id);

-- Audit log table (optional, for tracking login/logout)
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    guid UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
    user_id BIGINT REFERENCES users (id),
    action VARCHAR(50) NOT NULL,  -- LOGIN, LOGOUT, CREATE, UPDATE, DELETE
    entity_type VARCHAR(100),
    entity_id BIGINT,
    changes TEXT,  -- JSON of what changed
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_logs_user_id ON audit_logs (user_id);
CREATE INDEX idx_audit_logs_created_at ON audit_logs (created_at);
CREATE INDEX idx_audit_logs_action ON audit_logs (action);
