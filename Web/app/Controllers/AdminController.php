<?php
declare(strict_types=1);

namespace Horizonte\Controllers;

use Horizonte\Core\Session;
use Horizonte\Core\View;
use Horizonte\Repositories\SiteContentRepository;

final class AdminController
{
    public function dashboard(): void
    {
        Session::start();
        $adminError = (string) ($_SESSION['admin_error'] ?? '');
        unset($_SESSION['admin_error']);
        if (empty($_SESSION['admin_logged_in'])) {
            View::render('admin/login', ['adminError' => $adminError]);
            return;
        }

        $siteContent = (new SiteContentRepository())->load();
        $events = is_array($siteContent['eventos'] ?? null) ? $siteContent['eventos'] : [];
        $successText = (string) ($_GET['status'] ?? '') === 'contenido' ? 'El contenido del sitio se actualizó.' : '';
        $warningText = '';

        View::render('admin/dashboard', compact('siteContent', 'events', 'successText', 'warningText'));
    }
}
