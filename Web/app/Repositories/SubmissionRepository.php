<?php
declare(strict_types=1);

namespace Horizonte\Repositories;

use Horizonte\Core\Database;

final class SubmissionRepository
{
    public function create(array $submission): void
    {
        $services = json_encode($submission['servicios'], JSON_UNESCAPED_UNICODE | JSON_THROW_ON_ERROR);
        $type = (string) $submission['tipo_evento'];
        $date = (string) $submission['fecha_tentativa'];
        $time = (string) $submission['horario'];
        $guests = (int) $submission['invitados'];
        $space = (string) $submission['espacio_preferido'];
        $comments = (string) $submission['comentarios'];
        $name = (string) $submission['nombre'];
        $surname = (string) $submission['apellido'];
        $email = (string) $submission['email'];
        $phone = (string) $submission['telefono'];
        $connection = Database::connection();
        $statement = $connection->prepare(
            'INSERT INTO solicitudes_web (tipo_evento, fecha_tentativa, horario, invitados, espacio_preferido, servicios, comentarios, nombre, apellido, email, telefono) '
            . 'VALUES (?, NULLIF(?, \'\'), NULLIF(?, \'\'), ?, NULLIF(?, \'\'), ?, NULLIF(?, \'\'), ?, ?, ?, NULLIF(?, \'\'))'
        );
        $statement->bind_param(
            'sssisssssss',
            $type, $date, $time, $guests, $space, $services,
            $comments, $name, $surname, $email, $phone,
        );
        $statement->execute();
        $statement->close();
    }

    public function recent(): array
    {
        $connection = Database::connection();
        $result = $connection->query('SELECT * FROM solicitudes_web ORDER BY fecha_creacion DESC LIMIT 50');
        return $result->fetch_all(MYSQLI_ASSOC);
    }

    public function recentForClient(string $email): array
    {
        $connection = Database::connection();
        $statement = $connection->prepare('SELECT id_solicitud, tipo_evento, fecha_tentativa, horario, invitados, estado, respuesta_admin, fecha_creacion FROM solicitudes_web WHERE email = ? ORDER BY fecha_creacion DESC LIMIT 20');
        $statement->bind_param('s', $email);
        $statement->execute();
        $rows = $statement->get_result()->fetch_all(MYSQLI_ASSOC);
        $statement->close();
        return $rows;
    }

    public function updateStatus(int $id, string $status): void
    {
        $connection = Database::connection();
        $statement = $connection->prepare('UPDATE solicitudes_web SET estado = ? WHERE id_solicitud = ?');
        $statement->bind_param('si', $status, $id);
        $statement->execute();
        $statement->close();
    }
}
