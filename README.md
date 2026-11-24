# Menú Restaurante – CRUD de Productos

Proyecto de aula · Ingeniería de Software · Politécnico Grancolombiano
**Integrantes:** Darikson Camargo Ayala · Edwin Steven Reyes

Aplicación web CRUD para la entidad **Producto** (ítems del menú de un restaurante) con arquitectura en capas.
Stack: Spring Boot 3.5 · Java 21 · Spring Data JPA/Hibernate · PostgreSQL · Swagger · Docker · SonarQube.
Patrón de diseño GoF: **Factory Method** (creación de productos por categoría).

## Estructura

```
src/main/java/co/edu/poligran/menurestaurante
├── controller   → Presentación (API REST)
├── service      → Negocio
│   └── factory  → Patrón Factory Method (ProductoCreador, creadores concretos y provider)
├── repository   → Acceso a datos
├── model        → Producto (abstracta) y subclases: Entrada, PlatoFuerte, Acompanamiento, Postre, Bebida
├── dto          → Datos de entrada y salida
├── mapper       → Conversión entidad ↔ DTO
├── exception    → Manejo de errores
└── config       → Configuración de Swagger
src/main/resources/static → Interfaz web (index.html, css, js)
```

## Patrón Factory Method

| Participante | Clase |
|---|---|
| Product | `Producto` (entidad abstracta, herencia `SINGLE_TABLE`, discriminador `categoria`) |
| ConcreteProduct | `Entrada`, `PlatoFuerte`, `Acompanamiento`, `Postre`, `Bebida` |
| Creator | `ProductoCreador` (método fábrica `crearProducto`) |
| ConcreteCreator | `EntradaCreador`, `PlatoFuerteCreador`, `AcompanamientoCreador`, `PostreCreador`, `BebidaCreador` |
| Selector | `ProductoCreadorProvider` |

Regla derivada: la categoría se define al crear el producto y no se puede cambiar (PUT con otra categoría responde 409).

## Ejecutar

```bash
docker compose up -d db      # base de datos
mvn spring-boot:run          # aplicación
```

Interfaz web: http://localhost:8080/
Swagger UI: http://localhost:8080/swagger-ui.html

Pruebas: `mvn test`

Todo en contenedores: `docker compose up -d --build`

## SonarQube

```bash
docker compose --profile calidad up -d sonarqube   # http://localhost:9000
mvn clean verify sonar:sonar -Dsonar.token=<TOKEN>
```
