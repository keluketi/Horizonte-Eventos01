<?php
declare(strict_types=1);

namespace Horizonte\Core;

final class Session
{
    public static function start(): void
    {
        if (session_status() === PHP_SESSION_ACTIVE) {
            return;
        }

        ini_set('session.use_strict_mode', '1');
        ini_set('session.cookie_httponly', '1');
        ini_set('session.cookie_samesite', 'Lax');
        if (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off') {
            ini_set('session.cookie_secure', '1');
        }
        session_start();
    }

    public static function authenticated(): bool
    {
        self::start();
        return !empty($_SESSION['cliente_id']) || !empty($_SESSION['empleado_id']);
    }

    public static function home(): string
    {
        self::start();
        return ($_SESSION['usuario_tipo'] ?? '') === 'empleado' ? 'panel-trabajador.php' : 'mi-cuenta.php';
    }

    public static function requireClient(): void
    {
        self::start();
        if (($_SESSION['usuario_tipo'] ?? '') === 'empleado' && !empty($_SESSION['empleado_id'])) {
            header('Location: panel-trabajador.php');
            exit;
        }
        if (($_SESSION['usuario_tipo'] ?? '') !== 'cliente' || empty($_SESSION['cliente_id'])) {
            header('Location: login.php');
            exit;
        }
    }

    public static function requireEmployee(): void
    {
        self::start();
        if (($_SESSION['usuario_tipo'] ?? '') === 'cliente' && !empty($_SESSION['cliente_id'])) {
            header('Location: mi-cuenta.php');
            exit;
        }
        if (($_SESSION['usuario_tipo'] ?? '') !== 'empleado' || empty($_SESSION['empleado_id'])) {
            header('Location: login.php');
            exit;
        }
    }
}
