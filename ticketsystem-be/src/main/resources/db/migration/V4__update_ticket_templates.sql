-- ==========================================
-- Flyway migration: V4__update_ticket_templates.sql
-- ==========================================

-- HARDWARE
UPDATE ticket_templates tt
SET category_id = (SELECT id FROM categories WHERE code = 'HW')
WHERE tt.name IN ('Cuffie', 'Mouse', 'Hard Disk');

-- SOFTWARE
UPDATE ticket_templates tt
SET category_id = (SELECT id FROM categories WHERE code = 'SW')
WHERE tt.name IN ('Errore applicativo', 'Installazione Software');

-- ACCOUNT
UPDATE ticket_templates tt
SET category_id = (SELECT id FROM categories WHERE code = 'ACC')
WHERE tt.name IN ('Reset Password', 'Creazione nuovo account');

-- NETWORK
UPDATE ticket_templates tt
SET category_id = (SELECT id FROM categories WHERE code = 'NW')
WHERE tt.name IN ('Connessione lenta', 'VPN non funzionante');

-- BILLING
UPDATE ticket_templates tt
SET category_id = (SELECT id FROM categories WHERE code = 'BL')
WHERE tt.name IN ('Richiesta Fattura', 'Errore addebito');
