CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    role VARCHAR(50) NOT NULL CHECK (role IN ('ROLE_USER', 'ROLE_ADMIN')),
    vip BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE parking_lots (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    total_slots INTEGER NOT NULL DEFAULT 0,
    available_slots INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE vehicles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    vehicle_number VARCHAR(255) NOT NULL UNIQUE,
    vehicle_type VARCHAR(50) NOT NULL CHECK (vehicle_type IN ('CAR', 'MOTORBIKE', 'VAN', 'TRUCK', 'EV')),
    brand VARCHAR(255),
    model VARCHAR(255),
    color VARCHAR(255)
);

CREATE TABLE parking_slots (
    id BIGSERIAL PRIMARY KEY,
    slot_number VARCHAR(255) NOT NULL,
    slot_type VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('AVAILABLE', 'RESERVED', 'OCCUPIED')),
    parking_lot_id BIGINT NOT NULL REFERENCES parking_lots(id) ON DELETE CASCADE,
    CONSTRAINT uk_slot_per_lot UNIQUE (parking_lot_id, slot_number)
);

CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    vehicle_id BIGINT NOT NULL REFERENCES vehicles(id),
    slot_id BIGINT NOT NULL REFERENCES parking_slots(id),
    booking_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    booking_status VARCHAR(50) NOT NULL CHECK (booking_status IN ('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED')),
    qr_code_base64 TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL UNIQUE REFERENCES bookings(id) ON DELETE CASCADE,
    amount NUMERIC(10, 2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL CHECK (payment_method IN ('CASH', 'CARD', 'ONLINE')),
    payment_status VARCHAR(50) NOT NULL CHECK (payment_status IN ('PENDING', 'PAID', 'FAILED', 'REFUNDED')),
    payment_date TIMESTAMP NOT NULL
);

CREATE TABLE waiting_queue (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    parking_lot_id BIGINT NOT NULL REFERENCES parking_lots(id) ON DELETE CASCADE,
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    notified BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uk_queue_user_lot UNIQUE (user_id, parking_lot_id)
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_vehicles_user ON vehicles(user_id);
CREATE INDEX idx_bookings_user ON bookings(user_id);
CREATE INDEX idx_bookings_date ON bookings(booking_date);
CREATE INDEX idx_bookings_status ON bookings(booking_status);
