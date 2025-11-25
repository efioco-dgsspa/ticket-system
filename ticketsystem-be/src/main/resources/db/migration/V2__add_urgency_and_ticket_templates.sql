-- ============================================================
-- Flyway Migration V2 (AGGIORNATO)
-- 1. Conversione TicketUrgency da ENUM Java a tabella tipologica
-- 2. Aggiunta tabella ticket_templates
-- 3. Inserimento template di esempio
-- ============================================================

-- ============================================================
-- 1️⃣ CREAZIONE TABELLINA TIPOLOGICA: ticket_urgency
-- ============================================================

CREATE TABLE IF NOT EXISTS ticket_urgency (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(50) NOT NULL UNIQUE, 
    description TEXT NOT NULL, 
    priority INT NOT NULL
);

-- Inserimento valori base
INSERT INTO ticket_urgency (name, description) VALUES
('BLOCKING', 'Non si può lavorare', 1),
('HIGH', 'Si può lavorare con molta difficoltà', 2),
('MEDIUM', 'Si può lavorare con qualche difficoltà', 3),
('LOW', 'Si può lavorare normalmente', 4)
ON CONFLICT DO NOTHING;

-- ============================================================
-- 2️⃣ MODIFICA TABELLA tickets: sostituzione enum
-- ============================================================

-- Rimuovo l’eventuale colonna ENUM/string urgency
ALTER TABLE tickets
DROP COLUMN IF EXISTS urgency;

-- Aggiungo la colonna urgency_id come FK verso ticket_urgency
ALTER TABLE tickets
ADD COLUMN urgency_id UUID NOT NULL 
    DEFAULT (SELECT id FROM ticket_urgency WHERE name = 'LOW');

ALTER TABLE tickets
ADD CONSTRAINT fk_ticket_urgency
FOREIGN KEY (urgency_id) REFERENCES ticket_urgency(id);

-- ============================================================
-- 3️⃣ TABELLA ticket_templates
-- ============================================================

CREATE TABLE IF NOT EXISTS ticket_templates (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    category_id UUID REFERENCES ticket_categories(id),
    name VARCHAR(100) NOT NULL,
    template_text TEXT NOT NULL,
    UNIQUE(category_id, name)
);

-- ============================================================
-- 4️⃣ Template HARDWARE
-- ============================================================

INSERT INTO ticket_templates (category_id, name, template_text)
SELECT c.id, 'Cuffie',
'Vorrei segnalare il malfunzionamento delle cuffie in dotazione. Non riesco più a sentire l’audio correttamente. È possibile ottenere una sostituzione?'
FROM ticket_categories c WHERE c.name = 'HARDWARE'
ON CONFLICT DO NOTHING;

INSERT INTO ticket_templates (category_id, name, template_text)
SELECT c.id, 'Mouse',
'Il mouse collegato al mio PC non risponde correttamente ai movimenti o ai clic. Chiedo gentilmente una verifica o un eventuale dispositivo sostitutivo.'
FROM ticket_categories c WHERE c.name = 'HARDWARE'
ON CONFLICT DO NOTHING;

INSERT INTO ticket_templates (category_id, name, template_text)
SELECT c.id, 'Hard Disk',
'Rilevo problemi con l’hard disk del mio PC: rumorosità, rallentamenti o mancate letture. Richiedo un controllo hardware e un eventuale intervento tecnico.'
FROM ticket_categories c WHERE c.name = 'HARDWARE'
ON CONFLICT DO NOTHING;

-- ============================================================
-- 5️⃣ Template SOFTWARE
-- ============================================================

INSERT INTO ticket_templates (category_id, name, template_text)
SELECT c.id, 'Errore applicativo',
'Sto riscontrando un errore durante l’utilizzo del software aziendale. Allego descrizione e screenshot.'
FROM ticket_categories c WHERE c.name = 'SOFTWARE'
ON CONFLICT DO NOTHING;

INSERT INTO ticket_templates (category_id, name, template_text)
SELECT c.id, 'Installazione Software',
'Richiedo installazione di un nuovo software necessario per svolgere attività lavorative.'
FROM ticket_categories c WHERE c.name = 'SOFTWARE'
ON CONFLICT DO NOTHING;

-- ============================================================
-- 6️⃣ Template ACCOUNT
-- ============================================================

INSERT INTO ticket_templates (category_id, name, template_text)
SELECT c.id, 'Reset Password',
'Richiedo reset password del mio account aziendale poiché non riesco ad accedere.'
FROM ticket_categories c WHERE c.name = 'ACCOUNT'
ON CONFLICT DO NOTHING;

INSERT INTO ticket_templates (category_id, name, template_text)
SELECT c.id, 'Creazione nuovo account',
'È necessario creare un nuovo account per un utente interno.'
FROM ticket_categories c WHERE c.name = 'ACCOUNT'
ON CONFLICT DO NOTHING;

-- ============================================================
-- 7️⃣ Template NETWORK
-- ============================================================

INSERT INTO ticket_templates (category_id, name, template_text)
SELECT c.id, 'Connessione lenta',
'La connessione risulta lenta o instabile compromettendo le attività lavorative.'
FROM ticket_categories c WHERE c.name = 'NETWORK'
ON CONFLICT DO NOTHING;

INSERT INTO ticket_templates (category_id, name, template_text)
SELECT c.id, 'VPN non funzionante',
'Non riesco a collegarmi alla VPN aziendale.'
FROM ticket_categories c WHERE c.name = 'NETWORK'
ON CONFLICT DO NOTHING;

-- ============================================================
-- 8️⃣ Template BILLING
-- ============================================================

INSERT INTO ticket_templates (category_id, name, template_text)
SELECT c.id, 'Richiesta Fattura',
'Richiedo assistenza per recuperare o verificare una fattura non trovata o errata.'
FROM ticket_categories c WHERE c.name = 'BILLING'
ON CONFLICT DO NOTHING;

INSERT INTO ticket_templates (category_id, name, template_text)
SELECT c.id, 'Errore addebito',
'Segnalo un possibile errore in un addebito recente e chiedo verifica.'
FROM ticket_categories c WHERE c.name = 'BILLING'
ON CONFLICT DO NOTHING;
