<?php
declare(strict_types=1);

namespace Horizonte\Models;

final class Employee extends Account
{
    public function __construct(
        int $id,
        string $name,
        string $surname,
        string $email,
        string $passwordHash,
        public readonly string $cuil,
        public readonly int $roleId,
        public readonly string $roleName,
    ) {
        parent::__construct($id, $name, $surname, $email, $passwordHash);
    }

    public function type(): string
    {
        return 'empleado';
    }
}
