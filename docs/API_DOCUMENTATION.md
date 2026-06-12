# ParkSmart AI API Documentation

Base URL: `http://localhost:8080/api`

All protected requests use:

```http
Authorization: Bearer <jwt>
```

Response envelope:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {}
}
```

## Auth

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| POST | `/auth/register` | Public | Register a ROLE_USER account |
| POST | `/auth/login` | Public | Login and receive JWT |

## CRUD Modules

| Module | Endpoints |
| --- | --- |
| Users | `GET /users`, `GET /users/{id}`, `POST /users`, `PUT /users/{id}`, `DELETE /users/{id}` |
| Vehicles | `GET /vehicles?search=&userId=`, `GET /vehicles/{id}`, `POST /vehicles`, `PUT /vehicles/{id}`, `DELETE /vehicles/{id}` |
| Parking Lots | `GET /parking-lots?search=`, `GET /parking-lots/{id}`, `POST /parking-lots`, `PUT /parking-lots/{id}`, `DELETE /parking-lots/{id}` |
| Parking Slots | `GET /parking-slots?parkingLotId=&status=`, `GET /parking-slots/{id}`, `POST /parking-slots`, `PUT /parking-slots/{id}`, `PATCH /parking-slots/{id}/status?status=AVAILABLE`, `DELETE /parking-slots/{id}` |
| Bookings | `GET /bookings?date=&status=&userId=`, `GET /bookings/{id}`, `POST /bookings`, `PUT /bookings/{id}`, `PATCH /bookings/{id}/cancel`, `GET /bookings/{id}/verify`, `DELETE /bookings/{id}` |
| Payments | `GET /payments?status=`, `GET /payments/{id}`, `POST /payments`, `PUT /payments/{id}`, `DELETE /payments/{id}` |
| Waiting Queue | `GET /waiting-queue`, `POST /waiting-queue/join`, `POST /waiting-queue/assign-next`, `DELETE /waiting-queue/{id}` |
| Analytics | `GET /admin/dashboard` |

Swagger UI is available at `/swagger-ui.html`.
