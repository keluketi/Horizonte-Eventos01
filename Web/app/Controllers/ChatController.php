<?php
declare(strict_types=1);

namespace Horizonte\Controllers;

use Horizonte\Repositories\ChatRepository;
use Throwable;

final class ChatController
{
    public function __construct(private readonly ChatRepository $chat = new ChatRepository())
    {
    }

    public function handle(): never
    {
        header('Content-Type: application/json; charset=utf-8');
        header('Cache-Control: no-store');
        try {
            $this->chat->ensureSchema();
            if ($_SERVER['REQUEST_METHOD'] === 'GET') {
                $this->respond(200, ['ok' => true, 'messages' => $this->chat->all()]);
            }
            if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
                header('Allow: GET, POST');
                $this->respond(405, ['ok' => false, 'message' => 'Método no permitido.']);
            }

            $action = $_POST['action'] ?? ($_GET['action'] ?? 'send');
            if ($action !== 'send') {
                $this->respond(400, ['ok' => false, 'message' => 'Acción no válida.']);
            }
            $name = $this->sanitize((string) ($_POST['name'] ?? ''), 80);
            $email = $this->sanitize((string) ($_POST['email'] ?? ''), 180);
            $message = $this->sanitize((string) ($_POST['message'] ?? ''), 1000);
            if ($name === '' || $email === '' || $message === '' || !filter_var($email, FILTER_VALIDATE_EMAIL)) {
                $this->respond(422, ['ok' => false, 'message' => 'Completá tus datos y el mensaje para enviar la consulta.']);
            }
            $this->chat->createClientMessage($name, $email, $message);
            $this->respond(200, ['ok' => true, 'message' => 'Tu mensaje se envió al equipo.']);
        } catch (Throwable $exception) {
            error_log('Horizonte chat API error: ' . $exception->getMessage());
            $this->respond(500, ['ok' => false, 'message' => 'No pudimos procesar el chat en este momento.']);
        }
    }

    private function sanitize(string $value, int $maxLength): string
    {
        $value = preg_replace('/\s+/', ' ', trim($value)) ?? '';
        return mb_substr($value, 0, $maxLength, 'UTF-8');
    }

    private function respond(int $status, array $body): never
    {
        http_response_code($status);
        echo json_encode($body, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES);
        exit;
    }
}
