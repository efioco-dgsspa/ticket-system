-- ===============================
-- Flyway migration: V1__init_data.sql
-- ===============================

-- Abilita estensione UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- -------------------------------
-- TABELLE BASE PER AUTENTICAZIONE
-- -------------------------------
CREATE TABLE IF NOT EXISTS permissions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS roles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS role_permissions (
    role_id UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permission_id UUID NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- -------------------------------
-- PERMISSIONS
-- -------------------------------
INSERT INTO permissions (name)
VALUES
('USER_VIEW'), ('USER_CREATE'), ('USER_UPDATE'), ('USER_DELETE'),
('TICKET_VIEW'), ('TICKET_CREATE'), ('TICKET_UPDATE'), ('TICKET_DELETE')
ON CONFLICT (name) DO NOTHING;

-- -------------------------------
-- ROLES
-- -------------------------------
INSERT INTO roles (name)
VALUES ('ADMIN'), ('OPERATOR'), ('USER'), ('SUPPORT_MANAGER')
ON CONFLICT (name) DO NOTHING;

-- -------------------------------
-- ROLE-PERMISSION associations
-- -------------------------------
-- ADMIN → tutti i permessi
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ADMIN'
ON CONFLICT DO NOTHING;

-- OPERATOR → può visualizzare e aggiornare i ticket
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN ('TICKET_VIEW', 'TICKET_UPDATE')
WHERE r.name = 'OPERATOR'
ON CONFLICT DO NOTHING;

-- USER → può visualizzare e modificare il proprio profilo, oltre a creare e visualizzare i propri ticket
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN ('USER_VIEW', 'USER_UPDATE', 'TICKET_CREATE', 'TICKET_VIEW', 'TICKET_UPDATE')
WHERE r.name = 'USER'
ON CONFLICT DO NOTHING;

-- SUPPORT_MANAGER → supervisiona utenti e tickets. Può aggiornare e chiudere i ticket
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN ('USER_CREATE', 'USER_VIEW', 'USER_UPDATE', 'USER_DELETE', 'TICKET_VIEW', 'TICKET_UPDATE', 'TICKET_DELETE')
WHERE r.name = 'SUPPORT_MANAGER'
ON CONFLICT DO NOTHING;

-- -------------------------------
-- USERS
-- -------------------------------
INSERT INTO users (username, email, password, active)
VALUES
('dios.onnipotente','dios@mail.com','$2a$10$faIm8kk1T8gHBotVeppcy.f5uZIN3rGUbupf5NvX3toNZrb1dcyfa',true),
('operator.john','operator@mail.com','$2a$10$tK6EevO2c2vkV.VOmA2.6.OGlob/fGln9bvS1xfqjGPJ0m2CNHitG',true),
('user.luca','user@mail.com','$2a$10$FSKvP./VJL4maFEoSQaVreDa5HG/ER7YKHQeScfuXxUcea8NDSReu',true),
('manager.paola','manager@mail.com','$2a$10$qaXyMVF6JzOGTJbfYmEeaOqAQ1XVZLBi341ngOfPZobXkgCiFzLIG',true)
ON CONFLICT (email) DO NOTHING;

-- -------------------------------
-- USER-ROLE associations
-- -------------------------------
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u JOIN roles r ON r.name = 'ADMIN' WHERE u.username = 'dios.onnipotente'
ON CONFLICT DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u JOIN roles r ON r.name = 'OPERATOR' WHERE u.username = 'operator.john'
ON CONFLICT DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u JOIN roles r ON r.name = 'USER' WHERE u.username = 'user.luca'
ON CONFLICT DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u JOIN roles r ON r.name = 'SUPPORT_MANAGER' WHERE u.username = 'manager.paola'
ON CONFLICT DO NOTHING;

-- -------------------------------
-- TICKET DOMAIN TABLES
-- -------------------------------

CREATE TABLE IF NOT EXISTS ticket_statuses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS ticket_categories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS tickets (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id VARCHAR(100) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status_id UUID NOT NULL REFERENCES ticket_statuses(id),
    category_id UUID REFERENCES ticket_categories(id),
    creator_id UUID REFERENCES users(id),
    assigned_to_id UUID REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    removed BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ticket_messages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    ticket_id UUID NOT NULL REFERENCES tickets(id) ON DELETE CASCADE,
    author_id UUID NOT NULL REFERENCES users(id),
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ticket_attachments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    message_id UUID NOT NULL REFERENCES ticket_messages(id) ON DELETE CASCADE,
    filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    content BYTEA NOT NULL
);

-- -------------------------------
-- INITIAL DATA FOR TICKETS
-- -------------------------------
INSERT INTO ticket_statuses (code, name, description)
VALUES
('OPEN', 'Aperto', 'Ticket appena creato e aperto'),
('IN_PROGRESS', 'In lavorazione', 'Ticket in lavorazione da un operatore'),
('RESOLVED', 'Risolto', 'Ticket risolto'),
('REOPENED', 'Riaperto', 'Ticket riaperto dopo chiusura'),
('CLOSED', 'Chiuso', 'Ticket chiuso definitivamente')
ON CONFLICT (code) DO NOTHING;

INSERT INTO ticket_categories (name)
VALUES ('SOFTWARE'), ('HARDWARE'), ('ACCOUNT'), ('NETWORK'), ('BILLING')
ON CONFLICT (name) DO NOTHING;
