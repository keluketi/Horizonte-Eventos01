<?php
declare(strict_types=1);

namespace Horizonte\Models;

final class Client extends Account
{
    public function type(): string
    {
        return 'cliente';
    }
}
