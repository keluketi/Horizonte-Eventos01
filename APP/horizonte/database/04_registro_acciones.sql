-- Ejecutar una sola vez en phpMyAdmin, dentro de la base horizonte_eventos.
-- Registra los clics de botones de la aplicación de escritorio para Auditoría (Gerencia).
CREATE TABLE IF NOT EXISTS registro_acciones (
    id_accion INT NOT NULL AUTO_INCREMENT,
    id_empleado INT NULL,
    empleado_nombre VARCHAR(150) NOT NULL,
    rol VARCHAR(80) NOT NULL,
    pantalla VARCHAR(150) NOT NULL,
    accion VARCHAR(250) NOT NULL,
    fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_accion),
    INDEX idx_registro_acciones_fecha (fecha_hora),
    INDEX idx_registro_acciones_empleado (id_empleado),
    CONSTRAINT fk_registro_acciones_empleado FOREIGN KEY (id_empleado)
        REFERENCES empleados (id_empleado) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
