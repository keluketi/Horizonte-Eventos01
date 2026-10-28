-- Agrega el campo para que el equipo responda desde la app de escritorio.
-- Ejecutar una sola vez en horizonte_eventos antes de usar el panel de solicitudes.
CREATE TABLE IF NOT EXISTS chat_mensajes (
    id_mensaje INT UNSIGNED NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(120) NOT NULL,
    email VARCHAR(180) NOT NULL,
    mensaje TEXT NOT NULL,
    es_admin TINYINT(1) NOT NULL DEFAULT 0,
    respuesta_a INT UNSIGNED NULL DEFAULT NULL,
    creado_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_mensaje),
    KEY idx_chat_respuesta (respuesta_a),
    KEY idx_chat_fecha (creado_en)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

ALTER TABLE solicitudes_web
    ADD COLUMN respuesta_admin TEXT NULL AFTER estado;
