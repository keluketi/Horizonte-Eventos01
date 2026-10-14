<?php
declare(strict_types=1);

namespace Horizonte\Core;

final class CsrfToken
{
    public static function value(): string
    {
        Session::start();
        if (empty($_SESSION['csrf_token'])) {
            $_SESSION['csrf_token'] = bin2hex(random_bytes(32));
        }
        return (string) $_SESSION['csrf_token'];
    }

    public static function isValid(?string $submitted): bool
    {
        Session::start();
        return is_string($submitted) && isset($_SESSION['csrf_token'])
            && hash_equals((string) $_SESSION['csrf_token'], $submitted);
    }
}
