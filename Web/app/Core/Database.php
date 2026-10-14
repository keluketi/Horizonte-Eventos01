<?php
declare(strict_types=1);

namespace Horizonte\Core;

use mysqli;
use RuntimeException;

final class Database
{
    public static function connection(): mysqli
    {
        mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
        $host = getenv('HORIZONTE_DB_HOST') ?: '127.0.0.1';
        $port = (int) (getenv('HORIZONTE_DB_PORT') ?: 3306);
        $name = getenv('HORIZONTE_DB_NAME') ?: 'horizonte_eventos';
        $user = getenv('HORIZONTE_DB_USER') ?: 'root';
        $password = getenv('HORIZONTE_DB_PASSWORD') ?: '';
        if ($name === '' || $user === '') {
            throw new RuntimeException('Faltan las credenciales de la base de datos.');
        }

        $connection = new mysqli($host, $user, $password, $name, $port);
        $connection->set_charset('utf8mb4');
        return $connection;
    }
}
