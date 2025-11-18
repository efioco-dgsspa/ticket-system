export const environment = {
  production: true,

  // ✅ Attenzione: nel container Angular non può usare "localhost"
  // quindi usiamo il nome del servizio Docker definito nel docker-compose (backend)
  apiBaseUrl: 'http://backend:8080/api',

  // 🔧 Opzionali
  enableDebug: false,
  appName: 'Ticket System',
};
