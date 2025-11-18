# 🧩 Ticket Management System – Spring Boot & Angular

## 📖 Descrizione del progetto

Questo progetto è una web app full-stack sviluppata in Spring Boot (backend) e Angular (frontend), che implementa un sistema completo per la gestione di ticket di supporto in un contesto aziendale.
L’app nasce come estensione evolutiva del progetto Spring Boot Security Implementation, mantenendone l’architettura di autenticazione e autorizzazione, ma ampliandone le funzionalità con un sistema di tracciamento dei ticket e la gestione del loro flusso di vita.

Il sistema consente di:

## 👤 Gestire utenti, ruoli e permessi con autenticazione JWT

## 🎫 Creare, assegnare e aggiornare ticket di supporto

## 💬 Gestire lo stato di avanzamento del ticket in modo collaborativo (utente ↔ operatore ↔ admin)

## 📎 Allegare documenti PDF o file ai ticket

## 📂 Filtrare, cercare e ordinare utenti e ticket tramite parametri dinamici

## 🔐 Gestire sicurezza, autorizzazioni e accessi tramite Spring Security e ruoli granulari

---

## ⚙️ Stack Tecnologico

## 🔧 Backend

Java 17+

Spring Boot 3+

Spring Security + JWT (Access & Refresh Token)

Spring Data JPA / Hibernate

PostgreSQL

Flyway per la gestione delle migrazioni

Swagger / OpenAPI per la documentazione delle API

Maven

## 🎨 Frontend

Angular 17+

Tailwind CSS + ShadCN UI

Lucide Icons

RxJS / HTTPClient per l’integrazione con il backend

Docker Compose per orchestrare backend, database e frontend

## 🗃️ Inizializzazione del Database

La struttura dati è gestita da Flyway, che si occupa di creare e popolare le seguenti tabelle principali:

users, roles, permissions → gestione autenticazione

tickets, ticket_statuses, categories → gestione ticket

ticket_messages, ticket_attachments → gestione comunicazioni e allegati

Al primo avvio, vengono inseriti utenti e ruoli predefiniti:

## 👤 Utente base: può creare e aggiornare i propri ticket

## 🧑‍🔧 Operatore: può gestire, rispondere e risolvere ticket

## 👑 Admin: può chiudere definitivamente ticket e gestire categorie/utenti

---

## 🔄 Flusso operativo dei Ticket

Creazione del ticket
L’utente sceglie una categoria, scrive una descrizione e può allegare un file (PDF o immagine).

Presa in carico
Un operatore visualizza la lista dei ticket “aperti” e può assegnarsene uno, inserendo una risposta o allegando un file.

Comunicazione
Utente e operatore possono scambiarsi messaggi e allegati, mantenendo la cronologia del ticket.

Risoluzione e chiusura

L’utente può confermare la risoluzione del problema.

L’operatore segna il ticket come “risolto”.

L’amministratore può infine chiuderlo definitivamente.

Riapertura
Se l’utente ritiene che il problema non sia stato risolto, può riaprire il ticket prima della chiusura definitiva.

---

## 📦 API e Swagger

Tutte le API REST sono documentate tramite Swagger UI, accessibile da:

http://localhost:8080/swagger-ui/index.html


È possibile autenticarsi tramite JWT direttamente da interfaccia per testare le rotte protette.

## 🐳 Esecuzione con Docker Compose

Il progetto fornisce un file docker-compose.yml per avviare automaticamente:

PostgreSQL

Backend Spring Boot

Frontend Angular

Eseguire:

docker compose up --build


Poi accedere a:

## 🔙 Backend → http://localhost:8080

## 🎨 Frontend → http://localhost:4200

## 🧩 Autenticazione e Sicurezza

L’autenticazione avviene tramite JWT Token con supporto a:

Refresh token

Password crittografate (BCrypt)

Controllo ruoli e permessi granulari

----

## 🧠 Possibili evoluzioni

Aggiunta di un sistema di notifiche in tempo reale (WebSocket)

Introduzione di un dashboard statistico per admin e operatori

Integrazione con servizi esterni (email, storage cloud per allegati)

---

## 👨‍💻 Autore

Progetto sviluppato da Emanuele Fioco come evoluzione del corso di approfondimento su Spring Security e Architetture Full Stack.

## 📜 Licenza

Distribuito liberamente per fini didattici e dimostrativi.
