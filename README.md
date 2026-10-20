# Menú Restaurante – CRUD de Productos

Proyecto de aula · Ingeniería de Software · Politécnico Grancolombiano
**Integrantes:** Darikson Camargo Ayala · Edwin Steven Reyes

Aplicación web CRUD para la entidad **Producto** (ítems del menú de un restaurante) con arquitectura en capas.
Stack: Spring Boot 3.5 · Java 21 · Spring Data JPA/Hibernate · PostgreSQL · Swagger · Docker · SonarQube.

## Estructura

```
src/main/java/co/edu/poligran/menurestaurante
├── controller   → Presentación (API REST)
├── service      → Negocio
├── repository   → Acceso a datos
├── model        → Entidad Producto
├── dto          → Datos de entrada y salida
├── mapper       → Conversión entidad ↔ DTO
├── exception    → Manejo de errores
└── config       → Configuración de Swagger
```

## Ejecutar

```bash
docker compose up -d db      # base de datos
mvn spring-boot:run          # aplicación
```

Swagger UI: http://localhost:8080/swagger-ui.html

Todo en contenedores: `docker compose up -d --build`

## SonarQube

```bash
docker compose --profile calidad up -d sonarqube   # http://localhost:9000
mvn clean verify sonar:sonar -Dsonar.token=<TOKEN>
```
