<?php
declare(strict_types=1);

namespace Horizonte\Controllers;

use Horizonte\Core\Session;
use Horizonte\Repositories\ChatRepository;
use Horizonte\Repositories\SiteContentRepository;
use Horizonte\Repositories\SubmissionRepository;

final class AdminActionsController
{
    private const ALLOWED_STATUS = ['Pendiente', 'Contactado', 'Aprobado', 'Rechazado'];

    public function handle(): never
    {
        Session::start();
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
            http_response_code(405);
            header('Allow: POST');
            $this->jsonError('Método no permitido.');
        }
        $action = (string) ($_POST['action'] ?? '');
        if ($action === 'login') {
            $expected = getenv('HORIZONTE_ADMIN_PASSWORD') ?: 'horizonte2026';
            if (hash_equals($expected, (string) ($_POST['password'] ?? ''))) {
                session_regenerate_id(true);
                $_SESSION['admin_logged_in'] = true;
                header('Location: admin.php');
                exit;
            }
            $_SESSION['admin_error'] = 'Contraseña incorrecta.';
            header('Location: admin.php');
            exit;
        }
        if ($action === 'logout') {
            unset($_SESSION['admin_logged_in'], $_SESSION['admin_error']);
            header('Location: admin.php');
            exit;
        }
        if (empty($_SESSION['admin_logged_in'])) {
            http_response_code(403);
            $this->jsonError('No autorizado.');
        }

        if ($action === 'update_status') {
            $id = (int) ($_POST['id_solicitud'] ?? 0);
            $status = (string) ($_POST['estado'] ?? '');
            if ($id <= 0 || !in_array($status, self::ALLOWED_STATUS, true)) {
                header('Location: admin.php?error=estado');
                exit;
            }
            (new SubmissionRepository())->updateStatus($id, $status);
            header('Location: admin.php?status=solicitud');
            exit;
        }
        if ($action === 'save_content') {
            $repository = new SiteContentRepository();
            $content = $repository->load();
            $submittedEvents = is_array($_POST['evento'] ?? null) ? $_POST['evento'] : [];
            foreach ($content['eventos'] as $index => &$event) {
                $entry = is_array($submittedEvents[$index] ?? null) ? $submittedEvents[$index] : [];
                foreach (['label', 'title', 'image', 'name'] as $field) {
                    $value = trim((string) ($entry[$field] ?? $event[$field] ?? ''));
                    if ($value !== '') {
                        $event[$field] = $value;
                    }
                }
            }
            unset($event);
            foreach (['hero_title', 'hero_text'] as $field) {
                $value = trim((string) ($_POST[$field] ?? ''));
                if ($value !== '') {
                    $content[$field] = $value;
                }
            }
            $repository->save($content);
            header('Location: admin.php?status=contenido');
            exit;
        }
        if ($action === 'reply_chat') {
            $id = (int) ($_POST['id_mensaje'] ?? 0);
            $message = trim((string) ($_POST['mensaje'] ?? ''));
            if ($id <= 0 || $message === '') {
                header('Location: admin.php?error=mensaje');
                exit;
            }
            $chat = new ChatRepository();
            $chat->ensureSchema();
            $chat->createAdminReply(mb_substr($message, 0, 1000, 'UTF-8'), $id);
            header('Location: admin.php?status=chat');
            exit;
        }

        http_response_code(400);
        $this->jsonError('Acción no válida.');
    }

    private function jsonError(string $message): never
    {
        header('Content-Type: application/json; charset=utf-8');
        echo json_encode(['ok' => false, 'message' => $message], JSON_UNESCAPED_UNICODE);
        exit;
    }
}
