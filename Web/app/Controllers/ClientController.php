<?php
declare(strict_types=1);

namespace Horizonte\Controllers;

use Horizonte\Core\CsrfToken;
use Horizonte\Core\Session;
use Horizonte\Core\View;
use Horizonte\Repositories\SubmissionRepository;

final class ClientController
{
    public function account(): void
    {
        Session::requireClient();
        View::render('client/account', [
            'name' => (string) ($_SESSION['usuario_nombre'] ?? ''),
            'csrfToken' => CsrfToken::value(),
            'requests' => (new SubmissionRepository())->recentForClient((string) ($_SESSION['usuario_email'] ?? '')),
        ]);
    }
}
