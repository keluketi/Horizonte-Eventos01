<?php
declare(strict_types=1);

namespace Horizonte\Controllers;

use Horizonte\Core\Session;
use Horizonte\Core\View;
use Horizonte\Repositories\ChatRepository;
use Horizonte\Repositories\SiteContentRepository;
use Horizonte\Repositories\SubmissionRepository;

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

        (new ChatRepository())->ensureSchema();
        $siteContent = (new SiteContentRepository())->load();
        $solicitudes = (new SubmissionRepository())->recent();
        $chatRows = (new ChatRepository())->recentForAdmin();
        $events = is_array($siteContent['eventos'] ?? null) ? $siteContent['eventos'] : [];
        $successText = [
            'solicitud' => 'Solicitud actualizada.',
            'contenido' => 'El contenido del sitio se actualizó.',
            'chat' => 'La respuesta del chat se envió correctamente.',
        ][(string) ($_GET['status'] ?? '')] ?? '';
        $warningText = [
            'estado' => 'No se pudo actualizar el estado.',
            'mensaje' => 'No se pudo enviar la respuesta.',
        ][(string) ($_GET['error'] ?? '')] ?? '';

        View::render('admin/dashboard', compact('siteContent', 'solicitudes', 'chatRows', 'events', 'successText', 'warningText'));
    }
}
