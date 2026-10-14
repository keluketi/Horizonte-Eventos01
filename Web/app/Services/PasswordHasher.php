<?php
declare(strict_types=1);

namespace Horizonte\Services;

final class PasswordHasher
{
    private const ITERATIONS = 120000;
    private const KEY_LENGTH = 32;
    private const SALT_LENGTH = 16;

    /** Produce el mismo formato PBKDF2-SHA256 que usa la aplicación Java. */
    public function hash(string $password): string
    {
        $salt = random_bytes(self::SALT_LENGTH);
        $hash = hash_pbkdf2('sha256', $password, $salt, self::ITERATIONS, self::KEY_LENGTH, true);
        return 'pbkdf2$' . self::ITERATIONS . '$' . base64_encode($salt) . '$' . base64_encode($hash);
    }

    public function verify(string $password, string $storedHash): bool
    {
        if (password_verify($password, $storedHash)) {
            return true;
        }

        $parts = explode('$', $storedHash);
        if (count($parts) !== 4 || $parts[0] !== 'pbkdf2' || !ctype_digit($parts[1])) {
            return false;
        }
        $iterations = (int) $parts[1];
        $salt = base64_decode($parts[2], true);
        $expected = base64_decode($parts[3], true);
        if ($iterations < 1 || $iterations > 1000000 || $salt === false || strlen($salt) !== self::SALT_LENGTH
            || $expected === false || strlen($expected) !== self::KEY_LENGTH) {
            return false;
        }

        $actual = hash_pbkdf2('sha256', $password, $salt, $iterations, self::KEY_LENGTH, true);
        return hash_equals($expected, $actual);
    }
}
