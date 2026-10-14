<?php

declare(strict_types=1);

header('Content-Type: application/json; charset=utf-8');
header('Cache-Control: no-store');

require __DIR__ . '/db.php';

function sanitizeText(string $value, int $maxLength): string
{
    $text = trim($value);
    if ($text === '') {
        return '';
    }

    $text = preg_replace('/\s+/', ' ', $text);
    return mb_substr($text, 0, $maxLength, 'UTF-8');
}

try {
    ensureChatTable();

    if ($_SERVER['REQUEST_METHOD'] === 'GET') {
        $db = horizonteDb();
        $result = $db->query('SELECT id_mensaje, nombre, email, mensaje, es_admin, respuesta_a, creado_en FROM chat_mensajes ORDER BY creado_en ASC LIMIT 50');
        $messages = $result ? $result->fetch_all(MYSQLI_ASSOC) : [];
        $db->close();

        echo json_encode(['ok' => true, 'messages' => $messages], JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES);
        exit;
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        http_response_code(405);
        echo json_encode(['ok' => false, 'message' => 'Método no permitido.'], JSON_UNESCAPED_UNICODE);
        exit;
    }

    $action = $_POST['action'] ?? ($_GET['action'] ?? 'send');
    if ($action === 'send') {
        $name = sanitizeText((string) ($_POST['name'] ?? ''), 80);
        $email = sanitizeText((string) ($_POST['email'] ?? ''), 180);
        $message = sanitizeText((string) ($_POST['message'] ?? ''), 1000);

        if ($name === '' || $email === '' || $message === '' || !filter_var($email, FILTER_VALIDATE_EMAIL)) {
            http_response_code(422);
            echo json_encode(['ok' => false, 'message' => 'Completá tus datos y el mensaje para enviar la consulta.'], JSON_UNESCAPED_UNICODE);
            exit;
        }

        $db = horizonteDb();
        $stmt = $db->prepare('INSERT INTO chat_mensajes (nombre, email, mensaje, es_admin, respuesta_a, creado_en) VALUES (?, ?, ?, 0, NULL, NOW())');
        $stmt->bind_param('sss', $name, $email, $message);
        if (!$stmt->execute()) {
            throw new RuntimeException('No se pudo guardar el mensaje.');
        }
        $stmt->close();
        $db->close();

        echo json_encode(['ok' => true, 'message' => 'Tu mensaje se envió al equipo.'], JSON_UNESCAPED_UNICODE);
        exit;
    }

    http_response_code(400);
    echo json_encode(['ok' => false, 'message' => 'Acción no válida.'], JSON_UNESCAPED_UNICODE);
} catch (Throwable $exception) {
    error_log('Horizonte chat API error: ' . $exception->getMessage());
    http_response_code(500);
    echo json_encode(['ok' => false, 'message' => 'No pudimos procesar el chat en este momento.'], JSON_UNESCAPED_UNICODE);
}
