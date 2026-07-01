# ms-catalogo

Microservicio de **catálogo de productos** de GGStore. Administra los juegos disponibles en la tienda, sus categorías y las reseñas que dejan los usuarios.

## Responsabilidad

- CRUD de juegos (título, precio, stock, categoría).
- CRUD de categorías.
- Reseñas de usuarios por juego.
- Descuento de stock cuando `ms-pedidos` confirma una compra.

Es un servicio **de solo datos**: no maneja autenticación ni conoce usuarios directamente (recibe operaciones ya autorizadas por `ms-bff`).

## Stack técnico

| Componente | Detalle |
|---|---|
| Lenguaje | Java 17 |
| Framework | Spring Boot (Web MVC) |
| Persistencia | Spring Data JPA + PostgreSQL |
| Validación | Spring Validation |
| Utilidades | Lombok |
| Tests | JUnit 5, Mockito, AssertJ, H2 (in-memory) |
| Cobertura | JaCoCo |

## Estructura del proyecto

```
src/main/java/com/ggstore/ms_catalogo/
├── controller/     # Endpoints REST (Juego, Categoria, Resena)
├── service/        # Lógica de negocio
├── repository/     # Interfaces Spring Data JPA
├── model/          # Entidades JPA
└── dto/            # Objetos de transferencia (JuegoDTO, ResenaDTO)
```

## Cómo levantarlo local

### Requisitos
- JDK 17
- Maven (o usar el wrapper `mvnw` / `mvnw.cmd` incluido)
- Acceso a una base PostgreSQL

### Variables de entorno / configuración

El archivo `src/main/resources/application.properties` define la conexión a la base y el puerto. **No debe contener credenciales reales versionadas** — se recomienda migrar estos valores a variables de entorno:

```properties
spring.application.name=ms-catalogo

spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=validate
server.port=8081
```

> ⚠️ El `application.properties` actual del repo tiene la URL, usuario y password de la base de Supabase hardcodeados. Antes de hacer el repo público (o de sumar más gente al equipo), conviene sacarlos a variables de entorno o a un `.env` ignorado por Git.

### Levantar el servicio

```bash
./mvnw spring-boot:run
```

El servicio queda disponible en `http://localhost:8081`.

## Endpoints principales

### Juegos — `/juegos`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/juegos` | Lista paginada, filtros opcionales `titulo`, `categoriaId`, `page`, `size` |
| GET | `/juegos/{id}` | Obtiene un juego por ID |
| POST | `/juegos` | Crea un juego |
| PUT | `/juegos/{id}` | Actualiza un juego |
| PATCH | `/juegos/{id}/stock?cantidad=` | Descuenta stock (usado por `ms-pedidos` tras un checkout) |
| DELETE | `/juegos/{id}` | Elimina un juego |

### Categorías — `/categorias`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/categorias` | Lista todas |
| GET | `/categorias/{id}` | Obtiene una categoría |
| POST | `/categorias` | Crea una categoría |
| DELETE | `/categorias/{id}` | Elimina una categoría |

### Reseñas — `/resenas`
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/resenas/juego/{juegoId}` | Lista reseñas de un juego |
| POST | `/resenas` | Crea una reseña |
| DELETE | `/resenas/{id}` | Elimina una reseña |

## Tests y cobertura

```bash
./mvnw test
```

Los tests usan Mockito puro sobre `controller` y `service` (sin levantar contexto de Spring), y una base H2 en memoria para los tests que sí necesitan persistencia.

El reporte de cobertura se genera automáticamente en:

```
target/site/jacoco/index.html
```

**Cobertura actual: ~99% instructions.**

## Servicios relacionados

- `ms-pedidos` llama a este servicio para consultar precio/stock de un juego y para descontar stock tras un checkout confirmado.
- `ms-bff` reenvía (proxy) las peticiones de `/juegos`, `/categorias` y `/resenas` hacia este servicio.
