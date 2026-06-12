# ParkSmart AI

Production-quality full-stack smart vehicle park booking system for universities, shopping malls, hospitals, and office buildings.

## Stack

- Backend: Java 21, Spring Boot 3, Spring Security, JWT, Spring Data JPA, PostgreSQL, Maven, Lombok, Validation API, Swagger, JUnit
- Frontend: React 19, Vite, React Router, Axios, Material UI, Recharts
- Database: PostgreSQL, ready for Neon

## Features

- JWT registration and login with BCrypt password hashing
- USER and ADMIN roles
- CRUD for users, vehicles, parking lots, parking slots, bookings, payments, and waiting queue entries
- Smart waiting queue with `Queue<User>`
- VIP booking priority with `PriorityQueue<Booking>`
- Fast slot lookup with `HashMap<Long, ParkingSlot>`
- Date-sorted booking grouping with `TreeMap<LocalDate, List<Booking>>`
- Dynamic pricing from peak hours, weekend, and occupancy
- QR code generation for bookings and QR verification endpoint
- Admin dashboard analytics with charts
- Search and filters on key resources
- Responsive Material UI frontend with dark mode
- SQL schema, ER diagram, Swagger docs, Docker configuration, and sample data seeder

## Folder Structure

```text
SmartPark/
  backend/
    src/main/java/com/parksmartai/
      config/ controller/ dto/ entity/ enums/ exception/ mapper/ repository/ security/ service/ util/
    src/main/resources/application.yml
    pom.xml
  frontend/
    src/api/ components/ context/ pages/ theme/
    package.json
  database/schema.sql
  docs/API_DOCUMENTATION.md
  docs/ER_DIAGRAM.md
  docker-compose.yml
```

## Environment

Create `.env` from `.env.example` and fill your Neon password/JWT secret.

Backend reads:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
CORS_ALLOWED_ORIGINS
```

Frontend reads:

```text
VITE_API_BASE_URL
```

## Deploy

### Backend on Render

1. Create a PostgreSQL database, for example on Neon.
2. In Render, create a new Blueprint from this repository. Render will use `render.yaml`.
3. Set these backend environment variables in Render:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
CORS_ALLOWED_ORIGINS=https://your-vercel-domain.vercel.app
```

`JPA_DDL_AUTO` defaults to `update` for deployment. Use `validate` after the database schema is stable.

### Frontend on Vercel

1. Import this repository into Vercel.
2. Keep the repository root as the project root. Vercel will use `vercel.json`.
3. Set:

```text
VITE_API_BASE_URL=https://your-render-service.onrender.com/api
```

After Vercel gives you a production URL, add that URL to the backend `CORS_ALLOWED_ORIGINS` value in Render.

## Run Backend

```bash
cd backend
mvn spring-boot:run
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

Seeded users:

```text
admin@parksmart.ai / admin123
user@parksmart.ai / user123
```

## Run Frontend

```bash
cd frontend
npm install
npm run dev
```

Open:

```text
http://localhost:5173
```

## Docker

```bash
copy .env.example .env
docker compose up --build
```

## Tests

```bash
cd backend
mvn test
```

## Database

The complete PostgreSQL schema is in `database/schema.sql`. Hibernate can also manage tables with `spring.jpa.hibernate.ddl-auto=update` during development.
