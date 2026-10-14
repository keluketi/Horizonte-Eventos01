<?php
declare(strict_types=1);

namespace Horizonte\Services;

use Horizonte\Models\Account;
use Horizonte\Repositories\AccountRepository;

final class AuthService
{
    public function __construct(
        private readonly AccountRepository $accounts,
        private readonly PasswordHasher $passwords,
    ) {
    }

    public function authenticate(string $identifier, string $password): ?Account
    {
        // Gerencia crea los usuarios de personal; no existe registro web de empleados.
        $employee = $this->accounts->findEmployeeByIdentifier($identifier);
        if ($employee && $this->passwords->verify($password, $employee->passwordHash)) {
            return $employee;
        }

        $client = $this->accounts->findClientByEmail($identifier);
        if ($client && $this->passwords->verify($password, $client->passwordHash)) {
            return $client;
        }
        return null;
    }

    public function registerClient(array $data, string $password): int
    {
        return $this->accounts->createClient($data, $this->passwords->hash($password));
    }
}
