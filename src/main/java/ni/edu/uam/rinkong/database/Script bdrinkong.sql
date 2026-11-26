-- Database: bd_rinkong

CREATE SCHEMA IF NOT EXISTS hotel;

-- Tabla habitacion
CREATE TABLE hotel.habitacion (
    id_habitacion BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    numero VARCHAR(10) NOT NULL UNIQUE,
    tipo VARCHAR(50) NOT NULL,
    precio_noche NUMERIC(10,2) NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'Disponible',

    CONSTRAINT chk_precio
        CHECK (precio_noche > 0),

	CONSTRAINT chk_estado_habitacion
    	CHECK (estado IN ('Disponible', 'Ocupada', 'Mantenimiento'))
);

-- Tabla huesped
CREATE TABLE hotel.huesped (
    id_huesped BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    correo VARCHAR(150) UNIQUE,
    documento VARCHAR(30) NOT NULL UNIQUE
);


-- Tabla reserva
CREATE TABLE hotel.reserva (
    id_reserva BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_habitacion BIGINT NOT NULL,
    id_huesped BIGINT NOT NULL,
    fecha_entrada DATE NOT NULL,
    fecha_salida DATE NOT NULL,
    fecha_reserva TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(30) NOT NULL DEFAULT 'Confirmada',

    CONSTRAINT fk_reserva_habitacion
        FOREIGN KEY (id_habitacion)
        REFERENCES hotel.habitacion(id_habitacion),

    CONSTRAINT fk_reserva_huesped
        FOREIGN KEY (id_huesped)
        REFERENCES hotel.huesped(id_huesped),

    CONSTRAINT chk_fechas_reserva
        CHECK (fecha_salida > fecha_entrada),

    CONSTRAINT chk_estado_reserva
        CHECK (estado IN ('Confirmada', 'Cancelada', 'Finalizada'))
);


-- Insercion habitaciones 
INSERT INTO hotel.habitacion
(numero, tipo, precio_noche, estado)
VALUES
('101', 'Individual', 45.00, 'Disponible'),
('102', 'Doble', 70.00, 'Disponible'),
('103', 'Suite', 120.00, 'Disponible'),
('104', 'Individual', 45.00, 'Disponible'),
('105', 'Doble', 70.00, 'Mantenimiento');

-- Insercion huespedes
INSERT INTO hotel.huesped
(nombre, apellido, telefono, correo, documento)
VALUES
('Carlos', 'Martinez', '88881111', 'carlos@example.com', 'DOC001'),
('Maria', 'Lopez', '88882222', 'maria@example.com', 'DOC002'),
('Andrea', 'Gomez', '88883333', 'andrea@example.com', 'DOC003');

