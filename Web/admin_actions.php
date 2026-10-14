<?php

declare(strict_types=1);

session_start();

require __DIR__ . '/db.php';
require __DIR__ . '/site_content.php';

const ALLOWED_STATUS = ['Pendiente', 'Contactado', 'Aprobado', 'Rechazado'];

function adminGuard(): void
{
    if (empty($_SESSION['admin_logged_in']) || $_SESSION['admin_logged_in'] !== true) {
        http_response_code(403);
        echo json_encode(['ok' => false, 'message' => 'No autorizado.'], JSON_UNESCAPED_UNICODE);
        exit;
    }
}

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(['ok' => false, 'message' => 'Método no permitido.'], JSON_UNESCAPED_UNICODE);
    exit;
}

$action = $_POST['action'] ?? '';

if ($action === 'login') {
    $password = (string) ($_POST['password'] ?? '');
    $expected = getenv('HORIZONTE_ADMIN_PASSWORD') ?: 'horizonte2026';
    if (hash_equals($expected, $password)) {
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

adminGuard();

if ($action === 'update_status') {
    $id = (int) ($_POST['id_solicitud'] ?? 0);
    $status = (string) ($_POST['estado'] ?? '');
    if ($id <= 0 || !in_array($status, ALLOWED_STATUS, true)) {
        header('Location: admin.php?error=estado');
        exit;
    }

    $db = horizonteDb();
    $stmt = $db->prepare('UPDATE solicitudes_web SET estado = ? WHERE id_solicitud = ?');
    $stmt->bind_param('si', $status, $id);
    $stmt->execute();
    $stmt->close();
    $db->close();

    header('Location: admin.php?status=solicitud');
    exit;
}

if ($action === 'save_content') {
    $content = loadSiteContent();
    $rawEvents = $_POST['evento'] ?? [];
    $eventArray = $content['eventos'] ?? [];

    foreach ($eventArray as $index => &$event) {
        $entry = $rawEvents[$index] ?? [];
        $event['label'] = trim((string) ($entry['label'] ?? $event['label'] ?? '')) ?: $event['label'];
        $event['title'] = trim((string) ($entry['title'] ?? $event['title'] ?? '')) ?: $event['title'];
        $event['image'] = trim((string) ($entry['image'] ?? $event['image'] ?? '')) ?: $event['image'];
        $event['name'] = trim((string) ($entry['name'] ?? $event['name'] ?? '')) ?: $event['name'];
    }
    unset($event);

    $content['hero_title'] = trim((string) ($_POST['hero_title'] ?? $content['hero_title'] ?? '')) ?: $content['hero_title'];
    $content['hero_text'] = trim((string) ($_POST['hero_text'] ?? $content['hero_text'] ?? '')) ?: $content['hero_text'];
    saveSiteContent($content);

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

    ensureChatTable();
    $db = horizonteDb();
    $stmt = $db->prepare('INSERT INTO chat_mensajes (nombre, email, mensaje, es_admin, respuesta_a, creado_en) VALUES (?, ?, ?, 1, ?, NOW())');
    $adminName = 'Equipo Horizonte';
    $adminEmail = 'admin@horizonte.eventos';
    $stmt->bind_param('sssi', $adminName, $adminEmail, $message, $id);
    $stmt->execute();
    $stmt->close();
    $db->close();

    header('Location: admin.php?status=chat');
    exit;
}

http_response_code(400);
echo json_encode(['ok' => false, 'message' => 'Acción no válida.'], JSON_UNESCAPED_UNICODE);
