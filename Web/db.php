<?php

declare(strict_types=1);

function horizonteDb(): mysqli
{
    $host = getenv('HORIZONTE_DB_HOST') ?: '127.0.0.1';
    $port = (int) (getenv('HORIZONTE_DB_PORT') ?: 3306);
    $database = getenv('HORIZONTE_DB_NAME') ?: 'HE';
    $username = getenv('HORIZONTE_DB_USER') ?: 'root';
    $password = getenv('HORIZONTE_DB_PASSWORD') ?: '';

    if ($database === '' || $username === '') {
        throw new RuntimeException('Faltan credenciales de la base de datos.');
    }

    $db = new mysqli($host, $username, $password, $database, $port);
    if ($db->connect_error) {
        throw new RuntimeException('No se pudo conectar a la base de datos: ' . $db->connect_error);
    }

    $db->set_charset('utf8mb4');
    return $db;
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
