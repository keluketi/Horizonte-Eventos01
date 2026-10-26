-- Migración aditiva para los módulos de Coordinación, Personal y solicitudes web.
-- Ejecutar una sola vez sobre horizonte_eventos. No reemplaza ni borra datos existentes.
-- Si el volcado horizonte_eventos (6).sql ya creó estas tablas, no hace falta ejecutar este archivo.

CREATE TABLE IF NOT EXISTS asignaciones_personal (
    id_asignacion INT NOT NULL AUTO_INCREMENT,
    id_empleado INT NOT NULL,
    id_evento INT NOT NULL,
    funcion VARCHAR(100) NOT NULL,
    tarea VARCHAR(255) NOT NULL,
    hora_inicio_asignada TIME NOT NULL,
    hora_fin_asignada TIME NOT NULL,
    hora_inicio_real TIME NULL,
    hora_fin_real TIME NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'Pendiente',
    observaciones TEXT NULL,
    PRIMARY KEY (id_asignacion),
    UNIQUE KEY uq_asignacion_empleado_evento_tarea (id_empleado, id_evento, tarea),
    KEY ix_asignaciones_personal_empleado (id_empleado),
    KEY ix_asignaciones_personal_evento (id_evento),
    CONSTRAINT fk_asignaciones_personal_empleado FOREIGN KEY (id_empleado)
        REFERENCES empleados (id_empleado),
    CONSTRAINT fk_asignaciones_personal_evento FOREIGN KEY (id_evento)
        REFERENCES eventos (id_evento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS solicitudes_web (
    id_solicitud BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    tipo_evento VARCHAR(80) NOT NULL,
    fecha_tentativa DATE NULL,
    horario VARCHAR(20) NULL,
    invitados INT UNSIGNED NOT NULL,
    espacio_preferido VARCHAR(120) NULL,
    servicios TEXT NOT NULL,
    comentarios VARCHAR(1500) NULL,
    nombre VARCHAR(80) NOT NULL,
    apellido VARCHAR(80) NOT NULL,
    email VARCHAR(160) NOT NULL,
    telefono VARCHAR(40) NULL,
    estado ENUM('Pendiente', 'Contactado', 'Aprobado', 'Rechazado') NOT NULL DEFAULT 'Pendiente',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_solicitud),
    KEY idx_solicitudes_email (email),
    KEY idx_solicitudes_fecha (fecha_creacion),
    KEY idx_solicitudes_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
