-- ===============================
-- Flyway migration: V3__remove_table_ticket_categories.sql
-- ===============================

-- 1️⃣ Elimina la tabella ticket_categories se esiste
DROP TABLE IF EXISTS ticket_categories CASCADE;

-- 2️⃣ Pulisce la tabella categories (se già contiene dati)
TRUNCATE TABLE categories RESTART IDENTITY CASCADE;

-- 3️⃣ Popola la tabella categories
INSERT INTO categories (id, code, description, name)
VALUES
    (gen_random_uuid(),'HW', 'Problemi relativi a dispositivi hardware','HARDWARE'),
    (gen_random_uuid(),'SW','Assistenza su software e applicazioni','SOFTWARE'),
    (gen_random_uuid(),'NW','Problemi rete, VPN o connettività','NETWORK'),
    (gen_random_uuid(),'ACC', 'Creazione o modifica account','ACCOUNT'),
    (gen_random_uuid(),'BL', 'Richieste di emissioni fatture e gestione addebiti','BILLING'),
    (gen_random_uuid(),'TA', 'Richieste per problemi generici','Assistenza Tecnica');
