<?php

declare(strict_types=1);

require_once __DIR__ . '/app/bootstrap.php';

function horizonteDb(): mysqli
{
    return \Horizonte\Core\Database::connection();
}

function ensureChatTable(): void
{
    try {
        $db = horizonteDb();
        $db->query("CREATE TABLE IF NOT EXISTS chat_mensajes (
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
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci");
        $db->close();
    } catch (Throwable $exception) {
        error_log('Horizonte chat schema error: ' . $exception->getMessage());
    }
}
