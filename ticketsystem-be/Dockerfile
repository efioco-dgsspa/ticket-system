# ========================================
# 🐳 Backend - Dockerfile (usa JAR già compilato)
# ========================================

# 🔥 Stage unico: runtime soltanto
FROM eclipse-temurin:17-jdk

# Imposta la directory di lavoro nel container
WORKDIR /app

# Copia il file JAR già compilato localmente
# 👉 Assicurati di aver prima eseguito:
#    mvn clean package -DskipTests
# in modo che target/*.jar esista.
COPY target/*.jar app.jar

# Espone la porta su cui gira Spring Boot
EXPOSE 8080

# Profilo attivo = docker
ENTRYPOINT ["java", "-jar", "app.jar"]
