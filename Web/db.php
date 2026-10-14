<?php
declare(strict_types=1);
require_once __DIR__ . '/app/bootstrap.php';

function horizonteDb(): mysqli
{
    return \Horizonte\Core\Database::connection();
}

function ensureChatTable(): void
{
    try {
        (new \Horizonte\Repositories\ChatRepository())->ensureSchema();
    } catch (Throwable $exception) {
        error_log('Horizonte chat schema error: ' . $exception->getMessage());
    }
}