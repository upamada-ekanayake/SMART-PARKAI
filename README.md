# SMART-PARKAI

SMART-PARKAI is a full-stack smart vehicle parking booking system. It includes a Spring Boot REST API, PostgreSQL persistence, JWT authentication, role-based access, QR booking verification, admin analytics, and a React/Vite dashboard frontend.

Live deployment:

- Frontend: `https://smart-parkai.vercel.app`
- Backend: `https://smart-parkai.onrender.com`
- API base URL: `https://smart-parkai.onrender.com/api`
- Swagger UI: `https://smart-parkai.onrender.com/swagger-ui.html`

## Tech Stack

Backend:

- Java 21
- Spring Boot 3
- Spring Security
- JWT authentication
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok
- Bean Validation
- Springdoc OpenAPI / Swagger
- JUnit

Frontend:

- React 19
- Vite
- React Router
- Axios
- Material UI
- Recharts

Database and deployment:

- PostgreSQL / Neon-ready
- Render backend deployment
- Vercel frontend deployment
- Docker support

## Main Features

- User registration and login with JWT
- BCrypt password hashing
- USER and ADMIN roles
- Parking lot management
- Parking slot management
- Vehicle management
- Booking management
- Payment management
- Waiting queue management
- VIP booking priority support
- Dynamic parking price calculation
- QR code generation for bookings
- QR booking verification endpoint
- Admin dashboard analytics
- Search and filters across key resources
- Responsive Material UI interface
- Dark mode support
- Swagger API documentation
- PostgreSQL schema documentation
- Render and Vercel deployment configuration

## Project Structure

```text
SMART-PARKAI/
  backend/
    Dockerfile
    pom.xml
    src/main/java/com/parksmartai/
      config/
      controller/
      dto/
      entity/
      enums/
      exception/
      mapper/
      repository/
      security/
      service/
      util/
    src/main/resources/application.yml
    src/test/
  frontend/
    vercel.json
    package.json
    index.html
    src/
      api/
      components/
      context/
      pages/
      theme/
  database/
    schema.sql
  docs/
    API_DOCUMENTATION.md
    ER_DIAGRAM.md
  docker-compose.yml
  render.yaml
  vercel.json
```

## Environment Variables

Never commit real `.env` files. The project ignores local env files through `.gitignore`.

Backend variables:

```env
DB_URL=jdbc:postgresql://<host>/<database>?sslmode=require
DB_USERNAME=<database-user>
DB_PASSWORD=<database-password>
JWT_SECRET=<strong-secret-at-least-32-characters>
JWT_EXPIRATION_MINUTES=1440
JPA_DDL_AUTO=update
CORS_ALLOWED_ORIGINS=http://localhost:5173,https://smart-parkai.vercel.app
```

Frontend variables:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

For production on Vercel:

```env
VITE_API_BASE_URL=https://smart-parkai.onrender.com/api
```

## Local Setup

### Prerequisites

- Java 21
- Maven
- Node.js 22 or later
- PostgreSQL database, or a Neon PostgreSQL connection string

### Backend

Create `backend/.env` locally or set the same variables in your terminal. The application reads environment variables through Spring configuration.

Run:

```bash
cd backend
mvn spring-boot:run
```

Backend URL:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

### Frontend

Create `frontend/.env`:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

Run:

```bash
cd frontend
npm install
npm run dev
```

Frontend URL:

```text
http://localhost:5173
```

## Demo Users

The backend seed data creates demo users when the database is empty.

```text
Admin:
admin@parksmart.ai / admin123

User:
user@parksmart.ai / user123
```

For a real production deployment, change or remove seeded demo credentials.

## API Overview

Base API path:

```text
/api
```

Auth endpoints:

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| POST | `/api/auth/register` | Public | Register a user |
| POST | `/api/auth/login` | Public | Login and receive a JWT |

Main modules:

| Module | Endpoints |
| --- | --- |
| Users | `/api/users` |
| Vehicles | `/api/vehicles` |
| Parking lots | `/api/parking-lots` |
| Parking slots | `/api/parking-slots` |
| Bookings | `/api/bookings` |
| Payments | `/api/payments` |
| Waiting queue | `/api/waiting-queue` |
| Admin analytics | `/api/admin/dashboard` |

Protected requests require:

```http
Authorization: Bearer <jwt>
```

More API details are in `docs/API_DOCUMENTATION.md`.

## Database

The SQL schema is documented in:

```text
database/schema.sql
```

The ER diagram is documented in:

```text
docs/ER_DIAGRAM.md
```

During deployment, `JPA_DDL_AUTO=update` lets Hibernate create or update tables. After the schema is stable, use `validate` for stricter production behavior.

## Deploy Backend on Render

Use this repository:

```text
https://github.com/upamada-ekanayake/SMART-PARKAI
```

Recommended Render settings:

```text
Language: Docker
Branch: main
Root Directory: backend
Dockerfile Path: ./Dockerfile
```

Render environment variables:

```env
DB_URL=jdbc:postgresql://<host>/<database>?sslmode=require
DB_USERNAME=<database-user>
DB_PASSWORD=<database-password>
JWT_SECRET=<strong-secret-at-least-32-characters>
JWT_EXPIRATION_MINUTES=1440
JPA_DDL_AUTO=update
CORS_ALLOWED_ORIGINS=https://smart-parkai.vercel.app
```

If you also test from local Vite:

```env
CORS_ALLOWED_ORIGINS=http://localhost:5173,https://smart-parkai.vercel.app
```

After changing Render environment variables, redeploy the backend.

## Deploy Frontend on Vercel

Import the same GitHub repository into Vercel.

Recommended Vercel settings:

```text
Framework Preset: Vite
Root Directory: frontend
Install Command: npm ci
Build Command: npm run build
Output Directory: dist
```

Vercel environment variable:

```env
VITE_API_BASE_URL=https://smart-parkai.onrender.com/api
```

After changing Vercel environment variables, redeploy the frontend.

## Docker

The repository includes Dockerfiles for both apps and a Compose file.

```bash
docker compose up --build
```

The Compose file expects backend environment variables from a root `.env` file.

## Testing

Backend tests:

```bash
cd backend
mvn test
```

Frontend production build:

```bash
cd frontend
npm ci
npm run build
```

## Common Deployment Issues

### CORS blocks login or registration

Browser console example:

```text
No 'Access-Control-Allow-Origin' header is present on the requested resource
```

Fix Render backend env:

```env
CORS_ALLOWED_ORIGINS=https://smart-parkai.vercel.app
```

Do not add a trailing slash.

Correct:

```text
https://smart-parkai.vercel.app
```

Wrong:

```text
https://smart-parkai.vercel.app/
```

### Vercel says `cd frontend: No such file or directory`

This happens when Vercel Root Directory is already set to `frontend`, but commands still include `cd frontend`.

Use:

```text
Install Command: npm ci
Build Command: npm run build
Output Directory: dist
```

### Backend cannot connect to database

Check Render environment variables:

```env
DB_URL
DB_USERNAME
DB_PASSWORD
```

For Neon, the JDBC URL should usually include:

```text
?sslmode=require
```

## Security Notes

- Do not commit `.env` files.
- Rotate database passwords if they are exposed in chat, screenshots, commits, or logs.
- Use a long random `JWT_SECRET`.
- Restrict `CORS_ALLOWED_ORIGINS` to trusted frontend domains.
- Change or remove demo credentials before production use.

## License

This project includes the repository license in `LICENSE`.
