<?php
declare(strict_types=1);

namespace Horizonte\Repositories;

use Horizonte\Core\Database;
use Horizonte\Models\Client;
use Horizonte\Models\Employee;

final class AccountRepository
{
    public function findEmployeeByIdentifier(string $identifier): ?Employee
    {
        $sql = 'SELECT e.id_empleado, e.nombre, e.apellido, e.email, e.cuil, e.password, e.id_rol, r.nombre_rol '
            . 'FROM empleados e INNER JOIN roles r ON r.id_rol = e.id_rol '
            . 'WHERE e.email = ? OR e.cuil = ? LIMIT 1';
        $connection = Database::connection();
        $statement = $connection->prepare($sql);
        $statement->bind_param('ss', $identifier, $identifier);
        $statement->execute();
        $row = $statement->get_result()->fetch_assoc();
        $statement->close();

        return $row ? new Employee(
            (int) $row['id_empleado'],
            (string) $row['nombre'],
            (string) $row['apellido'],
            (string) $row['email'],
            (string) $row['password'],
            (string) $row['cuil'],
            (int) $row['id_rol'],
            (string) $row['nombre_rol'],
        ) : null;
    }

    public function findClientByEmail(string $email): ?Client
    {
        $connection = Database::connection();
        $statement = $connection->prepare(
            'SELECT id_cliente, nombre, apellido, email, password FROM clientes WHERE email = ? LIMIT 1'
        );
        $statement->bind_param('s', $email);
        $statement->execute();
        $row = $statement->get_result()->fetch_assoc();
        $statement->close();

        return $row ? new Client(
            (int) $row['id_cliente'],
            (string) $row['nombre'],
            (string) $row['apellido'],
            (string) $row['email'],
            (string) $row['password'],
        ) : null;
    }

    public function createClient(array $data, string $passwordHash): int
    {
        $name = (string) $data['nombre'];
        $surname = (string) $data['apellido'];
        $dni = (string) $data['dni_cuit'];
        $email = (string) $data['email'];
        $phone = (string) $data['telefono'];
        $connection = Database::connection();
        $statement = $connection->prepare(
            'INSERT INTO clientes (nombre, apellido, dni_cuit, email, telefono, password) '
            . 'VALUES (?, ?, ?, ?, NULLIF(?, \'\'), ?)'
        );
        $statement->bind_param('ssssss', $name, $surname, $dni, $email, $phone, $passwordHash);
        $statement->execute();
        $id = (int) $statement->insert_id;
        $statement->close();
        return $id;
    }

}
