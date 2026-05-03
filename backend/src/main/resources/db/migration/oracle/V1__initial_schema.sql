-- V1__initial_schema.sql - Oracle version
-- Create roles table
CREATE TABLE roles (
	id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	guid RAW(16) UNIQUE NOT NULL,
	name VARCHAR2(50 CHAR) UNIQUE NOT NULL,
	description VARCHAR2(255 CHAR),
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by VARCHAR2(255 CHAR) NOT NULL,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by VARCHAR2(255 CHAR) NOT NULL,
	deleted_at TIMESTAMP
);

CREATE INDEX idx_roles_deleted_at ON roles(deleted_at);
CREATE INDEX idx_roles_created_at ON roles(created_at);

-- Create users table
CREATE TABLE users (
	id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	guid RAW(16) UNIQUE NOT NULL,
	email VARCHAR2(255 CHAR) UNIQUE NOT NULL,
	password_hash VARCHAR2(255 CHAR) NOT NULL,
	first_name VARCHAR2(100 CHAR) NOT NULL,
	last_name VARCHAR2(100 CHAR) NOT NULL,
	is_active NUMBER(1) DEFAULT 1 NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by VARCHAR2(255 CHAR) NOT NULL,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by VARCHAR2(255 CHAR) NOT NULL,
	deleted_at TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_deleted_at ON users(deleted_at);
CREATE INDEX idx_users_created_at ON users(created_at);

-- Create user_roles junction table
CREATE TABLE user_roles (
	user_id NUMBER NOT NULL,
	role_id NUMBER NOT NULL,
	PRIMARY KEY (user_id, role_id),
	FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
	FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Create audit_logs table
CREATE TABLE audit_logs (
	id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	guid RAW(16) UNIQUE NOT NULL,
	user_id NUMBER,
	email VARCHAR2(255 CHAR),
	action VARCHAR2(100 CHAR) NOT NULL,
	description VARCHAR2(500 CHAR),
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	created_by VARCHAR2(255 CHAR) NOT NULL,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_by VARCHAR2(255 CHAR) NOT NULL,
	deleted_at TIMESTAMP,
	FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX idx_audit_logs_deleted_at ON audit_logs(deleted_at);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);
CREATE INDEX idx_audit_logs_user_id ON audit_logs(user_id);
