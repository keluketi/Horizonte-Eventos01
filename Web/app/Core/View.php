<?php
declare(strict_types=1);

namespace Horizonte\Core;

use RuntimeException;

final class View
{
    public static function render(string $template, array $data = []): void
    {
        $path = dirname(__DIR__, 2) . '/views/' . $template . '.php';
        if (!is_file($path)) {
            throw new RuntimeException('No se encontró la vista solicitada.');
        }
        extract($data, EXTR_SKIP);
        require $path;
    }

    public static function escape(string $value): string
    {
        return htmlspecialchars($value, ENT_QUOTES | ENT_SUBSTITUTE, 'UTF-8');
    }
}
