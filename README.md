# PartidoYA

Plataforma web para organizar y unirse a partidos deportivos casuales (fútbol, básquet, vóley, tenis), con reserva de canchas y pagos integrados.

**Demo en producción:** [partidoya.pro](https://partidoya.pro)

## Arquitectura

Backend en Java/Spring Boot siguiendo Domain-Driven Design, organizado en bounded contexts:

- `iam` — usuarios, autenticación (JWT), roles (`JUGADOR`, `ADMIN_CANCHA`) y planes de suscripción.
- `matches` — creación, unión, cancelación y solicitudes de pago de partidos.
- `profiles` — perfil del jugador (deporte principal, nivel, preferencias).
- `courts` — canchas y reservas gestionadas por administradores.
- `payments` — configuración de la pasarela de pagos (Culqi) y comprobantes de pago por Yape.

Cada contexto sigue capas `domain → application → infrastructure → interfaces`, con Value Objects como records de Java, CQRS (servicios de comando y consulta separados), y agregados persistence-ignorant (entidades JPA y ensambladores separados del modelo de dominio).

Frontend en Angular (standalone components), consumiendo la API REST vía HTTPS/JSON.

## Tech stack

**Backend**
- Java 21, Spring Boot 4.0.6
- Spring Data JPA + MySQL
- JWT (jjwt) para autenticación
- SpringDoc OpenAPI (Swagger UI)

**Frontend**
- Angular (esbuild `@angular/build:application`)
- TypeScript, SCSS
- Google Maps JavaScript API (autocompletado de direcciones, rutas y geolocalización)

**Infraestructura**
- Backend + MySQL desplegados en Railway
- Frontend desplegado en Vercel
- Pagos vía Culqi

## Funcionalidades principales

- Registro/login con roles diferenciados: jugador vs. administrador de cancha.
- Creación de partidos con dirección autocompletada (Google Places) y ubicación guardada.
- Panel de inicio con partidos cercanos por geolocalización y "tus partidos" (donde ya estás inscrito).
- Detalle de partido con mapa de ruta ("Cómo llegar") calculado automáticamente desde la ubicación del jugador.
- Gestión de canchas y reservas para administradores.
- Pago de partidos vía Culqi o comprobante de Yape con aprobación del organizador.

## Correr el proyecto localmente

### Backend
Requiere MySQL corriendo en `localhost:3306` (o ajustar `application-dev.properties`).

```bash
mvn spring-boot:run
```

- API: `http://localhost:8080/api/v1`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

### Frontend
```bash
cd frontend
npm install
ng serve
```

- App: `http://localhost:4200`
