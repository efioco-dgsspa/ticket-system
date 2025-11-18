# ========================================
# 🐳 Frontend - Dockerfile
# ========================================
# Fase 1: Build Angular
FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --configuration development

# Fase 2: Serve statico con Nginx
FROM nginx:alpine
COPY --from=build /app/dist/ticketsystem-fe /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
