<?php
declare(strict_types=1);

namespace Horizonte\Repositories;

use Horizonte\Core\Database;

final class ChatRepository
{
    public function ensureSchema(): void
    {
        Database::connection()->query("CREATE TABLE IF NOT EXISTS chat_mensajes (
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
    }

    public function all(): array
    {
        $result = Database::connection()->query(
            'SELECT id_mensaje, nombre, email, mensaje, es_admin, respuesta_a, creado_en FROM chat_mensajes ORDER BY creado_en ASC LIMIT 50'
        );
        return $result->fetch_all(MYSQLI_ASSOC);
    }

    public function recentForAdmin(): array
    {
        $result = Database::connection()->query('SELECT * FROM chat_mensajes ORDER BY creado_en ASC');
        return $result->fetch_all(MYSQLI_ASSOC);
    }

    public function createClientMessage(string $name, string $email, string $message): void
    {
        $statement = Database::connection()->prepare(
            'INSERT INTO chat_mensajes (nombre, email, mensaje, es_admin, respuesta_a, creado_en) VALUES (?, ?, ?, 0, NULL, NOW())'
        );
        $statement->bind_param('sss', $name, $email, $message);
        $statement->execute();
        $statement->close();
    }

    public function createAdminReply(string $message, int $replyTo): void
    {
        $adminName = 'Equipo Horizonte';
        $adminEmail = 'admin@horizonte.eventos';
        $statement = Database::connection()->prepare(
            'INSERT INTO chat_mensajes (nombre, email, mensaje, es_admin, respuesta_a, creado_en) VALUES (?, ?, ?, 1, ?, NOW())'
        );
        $statement->bind_param('sssi', $adminName, $adminEmail, $message, $replyTo);
        $statement->execute();
        $statement->close();
    }
}
