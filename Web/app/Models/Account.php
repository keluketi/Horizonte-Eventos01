<?php
declare(strict_types=1);

namespace Horizonte\Models;

abstract class Account
{
    public function __construct(
        public readonly int $id,
        public readonly string $name,
        public readonly string $surname,
        public readonly string $email,
        public readonly string $passwordHash,
    ) {
    }

    abstract public function type(): string;
}
